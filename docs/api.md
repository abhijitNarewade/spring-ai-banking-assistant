# API

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

All protected endpoints require:

```text
X-API-Key: dev-api-key
```

## Endpoints

| Method | Path | Purpose |
| --- | --- | --- |
| POST | `/api/v1/assistant/chat` | RAG answer with citations and audit ID |
| POST | `/api/v1/assistant/chat/stream` | Server-sent events streaming response |
| POST | `/api/v1/documents/ingest` | Ingest an approved banking document |
| GET | `/actuator/health` | Health probe |
