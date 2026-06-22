package com.banking.ai.dto;

import com.banking.ai.domain.AuditOutcome;
import com.banking.ai.domain.ChatProvider;

import java.time.Instant;

public record AuditRecord(
        String auditId,
        String sessionId,
        ChatProvider provider,
        AuditOutcome outcome,
        String questionHash,
        int citationCount,
        Instant createdAt) {
}
