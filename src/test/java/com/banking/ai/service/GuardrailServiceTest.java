package com.banking.ai.service;

import com.banking.ai.config.BankingAiProperties;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GuardrailServiceTest {

    @Test
    void blocksSensitiveAuthenticationRequests() {
        BankingAiProperties properties = new BankingAiProperties();
        properties.getGuardrails().setBlockedPatterns(List.of("(?i).*\\b(otp|pin|cvv|password)\\b.*"));
        GuardrailService guardrailService = new GuardrailService(properties);

        assertThat(guardrailService.isBlocked("My OTP is 123456, can you check it?")).isTrue();
        assertThat(guardrailService.isBlocked("How does NEFT work?")).isFalse();
    }
}
