CREATE TABLE sprint
(
    id          UUID        NOT NULL DEFAULT uuid_generate_v4(),
    number      SERIAL      NOT NULL,
    title       TEXT        NOT NULL,
    description TEXT        NOT NULL,
    deadline    TIMESTAMPTZ NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);

CREATE TABLE sprint_issue
(
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    sprint_id UUID NOT NULL,
    issue_id UUID NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (sprint_id, issue_id),
    FOREIGN KEY (sprint_id) REFERENCES sprint(id),
    FOREIGN KEY (issue_id) REFERENCES issue(id)
);
