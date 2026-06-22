package com.banking.ai.dto;

import com.banking.ai.domain.AuditOutcome;
import com.banking.ai.domain.ChatProvider;

import java.time.Instant;
import java.util.List;

public record AssistantResponse(
        String answer,
        ChatProvider provider,
        AuditOutcome outcome,
        List<Citation> citations,
        String auditId,
        Instant answeredAt) {
}
