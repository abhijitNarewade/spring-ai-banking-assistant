package com.banking.ai.dto;

import com.banking.ai.domain.KnowledgeCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IngestionRequest(
        @NotBlank String documentId,
        @NotBlank String title,
        @NotNull KnowledgeCategory category,
        @NotBlank String source,
        @NotBlank String content) {
}
