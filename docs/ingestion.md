# Document Ingestion

Approved banking documents can be ingested through the REST API.

```bash
curl -X POST http://localhost:8080/api/v1/documents/ingest \
  -H "Content-Type: application/json" \
  -H "X-API-Key: dev-api-key" \
  -d '{
    "documentId": "loan-faq-v1",
    "title": "Loan FAQs",
    "category": "LOAN_FAQ",
    "source": "internal-policy-repository",
    "content": "Eligibility depends on income, credit history, repayment capacity, KYC, and internal policy checks."
  }'
```

The service chunks the document, generates embeddings through Spring AI, and
writes vectors into PostgreSQL with pgvector.
