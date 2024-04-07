--liquibase formatted sql

--changeset vmaleev:1
ALTER TABLE product
ADD COLUMN image VARCHAR(64);

--changeset vmaleev:2
ALTER TABLE product_aud
    ADD COLUMN image VARCHAR(64);