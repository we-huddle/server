DROP TABLE IF EXISTS notification;
DROP TYPE IF EXISTS NOTIFICATION_TYPE;
CREATE TYPE NOTIFICATION_TYPE AS ENUM ('BADGE', 'TASK');

CREATE TABLE notification
(
    id              UUID                    NOT NULL DEFAULT uuid_generate_v4(),
    profileId       UUID                    NOT NULL,
    linkId          UUID                    NOT NULL,
    type            NOTIFICATION_TYPE       NOT NULL,
    title           TEXT                    NOT NULL,
    description     TEXT                    NOT NULL,
    read_status     BOOLEAN                 NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ             NOT NULL,
    updated_at      TIMESTAMPTZ             NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (profileId) REFERENCES profile (id)
);
