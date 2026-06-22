package com.banking.ai.dto;

import com.banking.ai.domain.ChatProvider;
import jakarta.validation.constraints.NotBlank;

public record AssistantRequest(
        @NotBlank String question,
        ChatProvider provider,
        String sessionId,
        boolean stream) {
}
