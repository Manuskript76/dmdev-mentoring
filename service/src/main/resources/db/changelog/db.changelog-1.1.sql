--liquibase formatted sql

--changeset vmaleev:1
CREATE TABLE IF NOT EXISTS revision
(
    id        BIGSERIAL PRIMARY KEY,
    timestamp BIGINT NOT NULL
);
--rollback DROP TABLE revision

--changeset vmaleev:2
CREATE TABLE IF NOT EXISTS client_aud
(
    id          BIGINT,
    rev         BIGINT REFERENCES revision (id),
    revtype     SMALLINT,
    firstname   VARCHAR(64),
    lastname    VARCHAR(64),
    email       VARCHAR(128),
    password    VARCHAR(128),
    phone       VARCHAR(32),
    address     VARCHAR(256),
    role        VARCHAR(32),
    created_at  TIMESTAMP,
    modified_at TIMESTAMP,
    created_by  VARCHAR(32),
    modified_by VARCHAR(32)
);
--rollback DROP TABLE client_aud

--changeset vmaleev:3
CREATE TABLE IF NOT EXISTS product_aud
(
    id           BIGINT,
    rev          BIGINT REFERENCES revision (id),
    revtype      SMALLINT,
    name         VARCHAR(64),
    description  VARCHAR,
    type         VARCHAR(64),
    manufacturer VARCHAR(64),
    cost         INT,
    quantity     INT,
    created_at   TIMESTAMP,
    modified_at  TIMESTAMP,
    created_by   VARCHAR(32),
    modified_by  VARCHAR(32)
);
--rollback DROP TABLE product_aud

--changeset vmaleev:4
CREATE SEQUENCE revinfo_seq
    increment by 1;
