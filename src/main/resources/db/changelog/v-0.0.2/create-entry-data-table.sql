-- liquibase formatted sql

-- changeset rdkaragaev:2 runAlways:true
CREATE TABLE entry (
    filename TEXT,
    line INT,
    data JSONB
)
