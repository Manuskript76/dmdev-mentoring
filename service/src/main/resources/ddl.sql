CREATE TABLE client
(
    id        BIGSERIAL PRIMARY KEY,
    firstname VARCHAR(64)  NOT NULL,
    lastname  VARCHAR(64)  NOT NULL,
    email     VARCHAR(128) NOT NULL UNIQUE,
    password  VARCHAR(128) NOT NULL,
    phone     VARCHAR(32)  NOT NULL,
    address   VARCHAR(256) NOT NULL,
    role      VARCHAR(32)  NOT NULL
);

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

CREATE TABLE order_product
(
    id           BIGSERIAL PRIMARY KEY,
    product_name BIGINT REFERENCES product (id),
    order_id     BIGINT REFERENCES client_order (id),
    quantity     INT NOT NULL
);

CREATE TABLE product
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(64) NOT NULL UNIQUE,
    description VARCHAR,
    cost        INT         NOT NULL,
    quantity    INT         NOT NULL
);

CREATE TABLE review
(
    id         BIGSERIAL PRIMARY KEY,
    client_id  BIGINT REFERENCES client (id),
    product_id BIGINT REFERENCES product (id),
    review     VARCHAR NOT NULL
);