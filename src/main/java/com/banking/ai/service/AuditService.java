package com.banking.ai.service;

import com.banking.ai.domain.AuditOutcome;
import com.banking.ai.domain.ChatProvider;
import com.banking.ai.dto.AuditRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class AuditService {

    private final JdbcTemplate jdbcTemplate;

    public AuditService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AuditRecord record(String sessionId, ChatProvider provider, AuditOutcome outcome,
                              String question, int citationCount) {
        String auditId = "AUD-" + UUID.randomUUID();
        String questionHash = sha256(question);
        Instant createdAt = Instant.now();
        jdbcTemplate.update("""
                INSERT INTO ai_audit_log
                (audit_id, session_id, provider, outcome, question_hash, citation_count, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """, auditId, sessionId, provider.name(), outcome.name(), questionHash, citationCount, createdAt);
        return new AuditRecord(auditId, sessionId, provider, outcome, questionHash, citationCount, createdAt);
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }
}
