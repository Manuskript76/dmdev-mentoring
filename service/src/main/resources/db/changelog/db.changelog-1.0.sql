--liquibase formatted sql

--changeset vmaleev:1
CREATE TABLE client
(
    id          BIGSERIAL PRIMARY KEY,
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
--rollback DROP TABLE client

--changeset vmaleev:2
CREATE TABLE client_order
(
    id            BIGSERIAL PRIMARY KEY,
    open_date     DATE        NOT NULL,
    close_date    DATE,
    status        VARCHAR(32) NOT NULL,
    client_id     BIGINT REFERENCES client (id),
    product_count INT,
    summary_cost  INT
);
--rollback DROP TABLE client_order

--changeset vmaleev:3
CREATE TABLE product
(
    id           BIGSERIAL PRIMARY KEY,
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
--rollback DROP TABLE product

--changeset vmaleev:4
CREATE TABLE order_product
(
    id         BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES product (id) ON DELETE CASCADE,
    order_id   BIGINT REFERENCES client_order (id) ON DELETE CASCADE,
    quantity   INT NOT NULL
);
--rollback DROP TABLE order_product

--changeset vmaleev:5
CREATE TABLE review
(
    id         BIGSERIAL PRIMARY KEY,
    client_id  BIGINT REFERENCES client (id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES product (id) ON DELETE CASCADE,
    review     VARCHAR NOT NULL,
    date       DATE    NOT NULL,
    grade      VARCHAR NOT NULL
);
--rollback DROP TABLE review