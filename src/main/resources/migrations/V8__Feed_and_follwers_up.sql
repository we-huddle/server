CREATE TABLE user_follower
(
    id         UUID        NOT NULL DEFAULT uuid_generate_v4(),
    profileId  UUID        NOT NULL,
    follows    UUID        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (profileId) REFERENCES profile (id),
    FOREIGN KEY (follows) REFERENCES profile (id)
);

CREATE TYPE EVENT_TYPE AS ENUM ('FOLLOWER', 'TASK', 'BADGE');

CREATE TABLE feed_event
(
    id          UUID        NOT NULL DEFAULT uuid_generate_v4(),
    profileId   UUID        NOT NULL,
    title       TEXT        NOT NULL,
    type        EVENT_TYPE  NOT NULL,
    referenceId UUID        NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL,
    updated_at  TIMESTAMPTZ NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (profileId) REFERENCES profile (id)
);
