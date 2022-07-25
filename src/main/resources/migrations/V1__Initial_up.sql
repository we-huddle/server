CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE USER_ROLE AS ENUM ('HUDDLER', 'HUDDLE_AGENT');

CREATE TABLE profile
(
    id               UUID        NOT NULL DEFAULT uuid_generate_v4(),
    name             TEXT        NOT NULL,
    github_username  TEXT        NOT NULL,
    email            TEXT        NOT NULL,
    photo            TEXT        NOT NULL,
    github_unique_id TEXT        NOT NULL,
    access_token     TEXT        NOT NULL,
    role             USER_ROLE   NOT NULL,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id)
);

CREATE TABLE session
(
    id         UUID        NOT NULL DEFAULT uuid_generate_v4(),
    profile_id UUID        NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id),
    FOREIGN KEY (profile_id) REFERENCES profile (id)
)
