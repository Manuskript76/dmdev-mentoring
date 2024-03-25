--liquibase formatted sql

--changeset vmaleev:1
CREATE TABLE IF NOT EXISTS revinfo
(
    rev      SERIAL PRIMARY KEY,
    revtstmp BIGINT NOT NULL
);

--changeset vmaleev:2
CREATE TABLE IF NOT EXISTS client_aud
(
    id          BIGINT,
    rev         BIGINT REFERENCES revinfo (rev),
    revtype     SMALLINT,
    firstname   VARCHAR(64)  NOT NULL,
    lastname    VARCHAR(64)  NOT NULL,
    email       VARCHAR(128) NOT NULL UNIQUE,
    password    VARCHAR(128) NOT NULL,
    phone       VARCHAR(32)  NOT NULL,
    address     VARCHAR(256) NOT NULL,
    role        VARCHAR(32)  NOT NULL,
    created_at  TIMESTAMP,
    modified_at TIMESTAMP,
    created_by  VARCHAR(32),
    modified_by VARCHAR(32)
);

--changeset vmaleev:3
CREATE TABLE IF NOT EXISTS product_aud
(
    id           BIGINT,
    rev          BIGINT REFERENCES revinfo (rev),
    revtype      SMALLINT,
    name         VARCHAR(64) NOT NULL UNIQUE,
    description  VARCHAR,
    type         VARCHAR(64) NOT NULL,
    manufacturer VARCHAR(64) NOT NULL,
    cost         INT         NOT NULL,
    quantity     INT         NOT NULL,
    created_at   TIMESTAMP,
    modified_at  TIMESTAMP,
    created_by   VARCHAR(32),
    modified_by  VARCHAR(32)
);

--changeset vmaleev:4
CREATE SEQUENCE revinfo_seq
    increment by 1;

--changeset vmaleev:5
CREATE TABLE IF NOT EXISTS revision
(
    id BIGSERIAL PRIMARY KEY,
    timestamp BIGINT NOT NULL
);