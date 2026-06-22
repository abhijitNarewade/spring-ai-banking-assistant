package com.banking.ai.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BankingPromptTemplatesTest {

    @Test
    void promptRequiresApprovedContext() {
        BankingPromptTemplates templates = new BankingPromptTemplates();

        assertThat(templates.systemPrompt()).contains("Use only the supplied context");
        assertThat(templates.userPrompt("What is RTGS?", "RTGS context")).contains("RTGS context");
    }
}
