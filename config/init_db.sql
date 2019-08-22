CREATE TABLE resume
(
    uuid      VARCHAR(36) PRIMARY KEY NOT NULL,
    full_name TEXT                    NOT NULL
);

CREATE TABLE contact
(
    id          SERIAL,
    resume_uuid VARCHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
    type        TEXT        NOT NULL,
    value       TEXT        NOT NULL
);
CREATE UNIQUE INDEX contact_uuid_type_index
    ON contact (resume_uuid, type);


/*
Next code is my preparing for adding "Organizations" and other sections


CREATE TABLE text_section
(
    id          SERIAL,
    resume_uuid VARCHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
    type        TEXT        NOT NULL,
    value       TEXT        NOT NULL
);
CREATE UNIQUE INDEX text_section_uuid_type_index
    ON text_section (resume_uuid, type);

CREATE TABLE organization_section
(
    id          SERIAL,
    resume_uuid VARCHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
    name        TEXT        NOT NULL,
    url         TEXT        NOT NULL,
    start_date  DATE        NOT NULL,
    end_date    DATE        NOT NULL,
    position    TEXT        NOT NULL,
    duties      TEXT        NOT NULL
);
CREATE UNIQUE INDEX organization_section_uuid_type_index
    ON organization_section (resume_uuid, name);
 */