package com.banking.ai.service;

import com.banking.ai.config.BankingAiProperties;
import com.banking.ai.domain.AuditOutcome;
import com.banking.ai.domain.ChatProvider;
import com.banking.ai.dto.AssistantRequest;
import com.banking.ai.dto.AssistantResponse;
import com.banking.ai.dto.AuditRecord;
import com.banking.ai.dto.Citation;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class RagAssistantService {

    private final VectorStore vectorStore;
    private final Map<String, ChatClient> chatClients;
    private final BankingAiProperties properties;
    private final GuardrailService guardrailService;
    private final BankingPromptTemplates promptTemplates;
    private final AuditService auditService;

    public RagAssistantService(VectorStore vectorStore,
                               Map<String, ChatClient> chatClients,
                               BankingAiProperties properties,
                               GuardrailService guardrailService,
                               BankingPromptTemplates promptTemplates,
                               AuditService auditService) {
        this.vectorStore = vectorStore;
        this.chatClients = chatClients;
        this.properties = properties;
        this.guardrailService = guardrailService;
        this.promptTemplates = promptTemplates;
        this.auditService = auditService;
    }

    public AssistantResponse answer(AssistantRequest request) {
        ChatProvider provider = request.provider() == null ? properties.getRag().getDefaultProvider() : request.provider();
        if (guardrailService.isBlocked(request.question())) {
            AuditRecord audit = auditService.record(request.sessionId(), provider, AuditOutcome.REFUSED_BY_GUARDRAIL,
                    request.question(), 0);
            return response(guardrailService.refusalMessage(), provider, AuditOutcome.REFUSED_BY_GUARDRAIL, List.of(), audit);
        }

        List<Document> matches = retrieve(request.question());
        List<Citation> citations = citations(matches);
        if (matches.isEmpty()) {
            AuditRecord audit = auditService.record(request.sessionId(), provider, AuditOutcome.NO_RELEVANT_CONTEXT,
                    request.question(), 0);
            return response(guardrailService.refusalMessage(), provider, AuditOutcome.NO_RELEVANT_CONTEXT, List.of(), audit);
        }

        String answer = generate(provider, request.question(), context(matches));
        AuditRecord audit = auditService.record(request.sessionId(), provider, AuditOutcome.ANSWERED,
                request.question(), citations.size());
        return response(answer, provider, AuditOutcome.ANSWERED, citations, audit);
    }

    private List<Document> retrieve(String question) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(question)
                .topK(properties.getRag().getTopK())
                .similarityThreshold(properties.getRag().getSimilarityThreshold())
                .build();
        return vectorStore.similaritySearch(searchRequest);
    }

    private String generate(ChatProvider provider, String question, String context) {
        ChatClient client = chatClients.get(beanName(provider));
        if (client == null) {
            throw new IllegalStateException("No ChatClient configured for " + provider);
        }
        return client.prompt()
                .system(promptTemplates.systemPrompt())
                .user(promptTemplates.userPrompt(question, context))
                .call()
                .content();
    }

    private String context(List<Document> documents) {
        StringBuilder builder = new StringBuilder();
        for (Document document : documents) {
            builder.append("Source: ")
                    .append(document.getMetadata().getOrDefault("title", "Untitled"))
                    .append(" [")
                    .append(document.getMetadata().getOrDefault("category", "UNKNOWN"))
                    .append("]\n")
                    .append(document.getText())
                    .append("\n\n");
        }
        return builder.toString();
    }

    private List<Citation> citations(List<Document> documents) {
        return documents.stream()
                .sorted(Comparator.comparing(document -> String.valueOf(document.getMetadata().getOrDefault("title", ""))))
                .map(document -> new Citation(
                        String.valueOf(document.getMetadata().getOrDefault("documentId", "unknown")),
                        String.valueOf(document.getMetadata().getOrDefault("title", "Untitled")),
                        String.valueOf(document.getMetadata().getOrDefault("category", "UNKNOWN")),
                        String.valueOf(document.getMetadata().getOrDefault("source", "internal")),
                        0.0))
                .toList();
    }

    private AssistantResponse response(String answer, ChatProvider provider, AuditOutcome outcome,
                                       List<Citation> citations, AuditRecord audit) {
        return new AssistantResponse(answer, provider, outcome, citations, audit.auditId(), Instant.now());
    }

    private String beanName(ChatProvider provider) {
        return provider == ChatProvider.ANTHROPIC ? "anthropicChatClient" : "openaiChatClient";
    }
}
