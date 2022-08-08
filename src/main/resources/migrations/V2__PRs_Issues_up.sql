CREATE TYPE PR_STATE AS ENUM ('open', 'closed');

CREATE TABLE pull_request
(
    id           UUID        NOT NULL DEFAULT uuid_generate_v4(),
    github_pr_id INTEGER     NOT NULL,
    profile_id   UUID        NULL,
    title        TEXT        NOT NULL,
    number       INTEGER     NOT NULL,
    state        PR_STATE    NOT NULL,
    url          TEXT        NOT NULL,
    merged       BOOLEAN     NOT NULL,
    github_user  JSONB       NOT NULL,
    assignees    JSONB       NULL,
    repo_name    TEXT        NOT NULL,
    repo_url     TEXT        NOT NULL,
    opened_at    TIMESTAMPTZ NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (profile_id) REFERENCES profile (id)
);

CREATE TYPE ISSUE_STATE AS ENUM ('open', 'closed');

CREATE TABLE issue
(
    id              UUID        NOT NULL DEFAULT uuid_generate_v4(),
    github_issue_id INTEGER     NOT NULL,
    title           TEXT        NOT NULL,
    number          INTEGER     NOT NULL,
    state           ISSUE_STATE NOT NULL,
    url             TEXT        NOT NULL,
    github_user     JSONB       NOT NULL,
    assignees       JSONB       NULL,
    repo_name       TEXT        NOT NULL,
    repo_url        TEXT        NOT NULL,
    opened_at       TIMESTAMPTZ NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE issue_assignment
(
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    issue_id UUID NOT NULL,
    profile_id UUID NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (issue_id, profile_id)
)
