# Guardrails

The assistant is designed for regulated retail banking contexts.

## Controls

- API-key protection for all business endpoints.
- Retrieval-first prompting that instructs the model to use only approved context.
- Refusal path when vector search returns no relevant context.
- Refusal path for secrets and sensitive identifiers such as OTP, PIN, CVV,
  passwords, PAN, Aadhaar, and full card numbers.
- Audit logging with question hashes rather than raw questions.
- Citations returned with every grounded answer.

## Hallucination Prevention

The service retrieves relevant documents from pgvector before generation. If no
documents pass the similarity threshold, the assistant returns the configured
refusal message instead of asking the model to answer from memory.
