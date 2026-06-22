package com.banking.ai.service;

import com.banking.ai.config.BankingAiProperties;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class GuardrailService {

    private final BankingAiProperties properties;

    public GuardrailService(BankingAiProperties properties) {
        this.properties = properties;
    }

    public boolean isBlocked(String question) {
        if (question == null || question.isBlank()) {
            return true;
        }
        return properties.getGuardrails().getBlockedPatterns().stream()
                .map(Pattern::compile)
                .anyMatch(pattern -> pattern.matcher(question).matches());
    }

    public String refusalMessage() {
        return properties.getRag().getRefusalMessage();
    }
}
