DROP TABLE IF EXISTS stats;

CREATE TABLE IF NOT EXISTS stats (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app VARCHAR(255),
    uri VARCHAR(255),
    ip VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE
    );