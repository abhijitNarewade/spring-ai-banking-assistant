package com.banking.ai.dto;

import java.time.Instant;

public record IngestionResponse(
        String documentId,
        int chunksCreated,
        Instant ingestedAt) {
}
