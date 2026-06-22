package com.banking.ai.domain;

public enum AuditOutcome {
    ANSWERED,
    REFUSED_BY_GUARDRAIL,
    NO_RELEVANT_CONTEXT,
    PROVIDER_ERROR
}
