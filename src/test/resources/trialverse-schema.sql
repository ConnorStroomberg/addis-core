SET DATABASE SQL SYNTAX PGS TRUE;
SET DATABASE SQL SIZE FALSE;

CREATE TABLE namespaces (id serial NOT NULL,
    name VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    PRIMARY KEY (id));
