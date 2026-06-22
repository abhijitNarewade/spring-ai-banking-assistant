package com.banking.ai.service;

import org.springframework.stereotype.Component;

@Component
public class BankingPromptTemplates {

    public String systemPrompt() {
        return """
                You are a retail banking assistant for a regulated financial institution.
                Use only the supplied context. If the context does not contain the answer, refuse briefly.
                Never ask for or process secrets such as OTP, PIN, CVV, passwords, PAN, Aadhaar, or full card numbers.
                Do not provide legal, tax, investment, or guaranteed approval advice.
                Include practical next steps and refer account-specific issues to authenticated banking channels.
                """;
    }

    public String userPrompt(String question, String context) {
        return """
                Customer question:
                %s

                Approved banking knowledge context:
                %s

                Answer with citations grounded in the context. Keep the tone clear, cautious, and customer-safe.
                """.formatted(question, context);
    }
}
