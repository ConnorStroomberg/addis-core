SET DATABASE SQL SYNTAX PGS TRUE;
SET DATABASE SQL SIZE FALSE;

CREATE TABLE namespaces (id serial NOT NULL,
    name VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    PRIMARY KEY (id));

CREATE TABLE studies (
    id bigint NOT NULL,
    name character varying NOT NULL,
    title text,
    PRIMARY KEY (id));

 --
 -- Name: namespace_studies; Type: TABLE; Schema: public; Owner: trialverse; Tablespace:
 --
 CREATE TABLE namespace_studies (
     namespace bigint NOT NULL,
     study bigint NOT NULL,
       PRIMARY KEY(namespace, study),
       FOREIGN KEY(namespace) REFERENCES namespaces(id),
       FOREIGN KEY(study) REFERENCES studies(id));

CREATE TABLE arms (
    id bigint NOT NULL,
    study bigint NOT NULL,
    name character varying,
    PRIMARY KEY(id),
    FOREIGN KEY(study) REFERENCES studies(id)
);

CREATE TABLE activities (
    id bigint NOT NULL,
    study bigint,
    name character varying,
    PRIMARY KEY(id),
    FOREIGN KEY(study) REFERENCES studies(id)
);

CREATE TABLE designs (
    arm bigint NOT NULL,
    activity bigint,
    epoch bigint,
    PRIMARY KEY(arm, activity),
    FOREIGN KEY(arm) REFERENCES arms(id),
    FOREIGN KEY(activity) REFERENCES activities(id)
);


CREATE TABLE treatments (
    id bigint NOT NULL,
    activity bigint,
    drug bigint,
    PRIMARY KEY(id),
    FOREIGN KEY(activity) REFERENCES activities(id)
);

CREATE TABLE measurement_moments (
    id bigint NOT NULL,
    study bigint,
    name character varying,
    epoch bigint,
    is_primary boolean,
    PRIMARY KEY(id),
    FOREIGN KEY(study) REFERENCES studies(id)
);

CREATE TABLE variables (
    id bigint NOT NULL,
    study bigint,
    name character varying,
    description VARCHAR,
    unit_description VARCHAR,
    is_primary boolean,
    measurement_type VARCHAR,
    variable_type VARCHAR,
    PRIMARY KEY(id),
    FOREIGN KEY(study) REFERENCES studies(id)
);

CREATE TABLE measurements (
    study bigint,
    variable bigint NOT NULL,
    measurement_moment bigint NOT NULL,
    arm bigint NOT NULL,
    attribute character varying NOT NULL,
    integer_value bigint,
    real_value double precision
);

CREATE TABLE namespace_mappings (
    id serial NOT NULL,
    uid VARCHAR NOT NULL
);