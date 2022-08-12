CREATE TYPE TASK_TYPE AS ENUM ('QUIZ', 'DEV');

CREATE TABLE task
(
    id          UUID        NOT NULL DEFAULT uuid_generate_v4(),
    title       TEXT        NOT NULL,
    description TEXT        NOT NULL,
    type        TASK_TYPE   NOT NULL,
    details     JSONB       NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL,
    updated_at  TIMESTAMPTZ NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (title)
);

CREATE TYPE ANSWER_STATUS AS ENUM ('COMPLETED', 'INCOMPLETE');

CREATE TABLE answer
(
    id         UUID          NOT NULL DEFAULT uuid_generate_v4(),
    profileId  UUID          NOT NULL,
    taskId     UUID          NOT NULL,
    status     ANSWER_STATUS NOT NULL,
    details    JSONB         NOT NULL,
    created_at TIMESTAMPTZ   NOT NULL,
    updated_at TIMESTAMPTZ   NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (profileId) REFERENCES profile (id),
    FOREIGN KEY (taskId) REFERENCES task (id)
);
