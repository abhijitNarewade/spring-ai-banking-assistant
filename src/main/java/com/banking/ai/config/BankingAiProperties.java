package com.banking.ai.config;

import com.banking.ai.domain.ChatProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "banking-ai")
public class BankingAiProperties {

    private final Security security = new Security();
    private final Rag rag = new Rag();
    private final Guardrails guardrails = new Guardrails();

    public Security getSecurity() {
        return security;
    }

    public Rag getRag() {
        return rag;
    }

    public Guardrails getGuardrails() {
        return guardrails;
    }

    public static class Security {
        private String apiKey = "dev-api-key";

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }
    }

    public static class Rag {
        private int topK = 5;
        private double similarityThreshold = 0.72;
        private ChatProvider defaultProvider = ChatProvider.OPENAI;
        private String refusalMessage = "I can only answer using approved banking knowledge sources.";

        public int getTopK() {
            return topK;
        }

        public void setTopK(int topK) {
            this.topK = topK;
        }

        public double getSimilarityThreshold() {
            return similarityThreshold;
        }

        public void setSimilarityThreshold(double similarityThreshold) {
            this.similarityThreshold = similarityThreshold;
        }

        public ChatProvider getDefaultProvider() {
            return defaultProvider;
        }

        public void setDefaultProvider(ChatProvider defaultProvider) {
            this.defaultProvider = defaultProvider;
        }

        public String getRefusalMessage() {
            return refusalMessage;
        }

        public void setRefusalMessage(String refusalMessage) {
            this.refusalMessage = refusalMessage;
        }
    }

    public static class Guardrails {
        private List<String> blockedPatterns = new ArrayList<>();

        public List<String> getBlockedPatterns() {
            return blockedPatterns;
        }

        public void setBlockedPatterns(List<String> blockedPatterns) {
            this.blockedPatterns = blockedPatterns;
        }
    }
}
