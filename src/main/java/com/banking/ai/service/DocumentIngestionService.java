package com.banking.ai.service;

import com.banking.ai.dto.IngestionRequest;
import com.banking.ai.dto.IngestionResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DocumentIngestionService {

    private static final int CHUNK_SIZE = 900;
    private static final int OVERLAP = 120;

    private final VectorStore vectorStore;

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public IngestionResponse ingest(IngestionRequest request) {
        List<Document> documents = chunk(request).stream()
                .map(chunk -> new Document(chunk.content(), Map.of(
                        "documentId", request.documentId(),
                        "title", request.title(),
                        "category", request.category().name(),
                        "source", request.source(),
                        "chunk", chunk.index())))
                .toList();

        vectorStore.add(documents);
        return new IngestionResponse(request.documentId(), documents.size(), Instant.now());
    }

    private List<TextChunk> chunk(IngestionRequest request) {
        String normalized = request.content().replaceAll("\\s+", " ").trim();
        List<TextChunk> chunks = new ArrayList<>();
        int index = 0;
        int start = 0;
        while (start < normalized.length()) {
            int end = Math.min(start + CHUNK_SIZE, normalized.length());
            chunks.add(new TextChunk(index++, normalized.substring(start, end)));
            if (end == normalized.length()) {
                break;
            }
            start = Math.max(0, end - OVERLAP);
        }
        return chunks;
    }

    private record TextChunk(int index, String content) {
    }
}
