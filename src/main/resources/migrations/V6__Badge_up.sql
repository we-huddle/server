CREATE TABLE badge
(
    id          UUID        NOT NULL DEFAULT uuid_generate_v4(),
    title       TEXT        NOT NULL,
    description TEXT        NOT NULL,
    photo       TEXT        NOT NULL,
    dep_badges  UUID[]      NOT NULL DEFAULT ARRAY []::UUID[],
    dep_tasks   UUID[]      NOT NULL DEFAULT ARRAY []::UUID[],
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);

CREATE TABLE badge_achievement
(
    id        UUID NOT NULL DEFAULT uuid_generate_v4(),
    profileId UUID NOT NULL,
    badgeId   UUID NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (id),
    FOREIGN KEY (profileId) REFERENCES profile (id),
    FOREIGN KEY (badgeId) REFERENCES badge (id)
);
