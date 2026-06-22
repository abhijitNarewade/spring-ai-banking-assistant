CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS ai_audit_log (
    id BIGSERIAL PRIMARY KEY,
    audit_id VARCHAR(80) NOT NULL UNIQUE,
    session_id VARCHAR(120),
    provider VARCHAR(40) NOT NULL,
    outcome VARCHAR(64) NOT NULL,
    question_hash VARCHAR(128) NOT NULL,
    citation_count INT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_ai_audit_log_created_at ON ai_audit_log(created_at);
CREATE INDEX IF NOT EXISTS idx_ai_audit_log_session_id ON ai_audit_log(session_id);
