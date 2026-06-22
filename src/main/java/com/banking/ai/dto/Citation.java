package com.banking.ai.dto;

public record Citation(
        String documentId,
        String title,
        String category,
        String source,
        double score) {
}
