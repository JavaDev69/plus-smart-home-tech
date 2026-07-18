CREATE TABLE IF NOT EXISTS products
(
    id               UUID PRIMARY KEY,
    product_name     VARCHAR(255)   NOT NULL,
    description      VARCHAR,
    image_src        VARCHAR(255),
    quantity_state   VARCHAR(30)    NOT NULL,
    product_state    VARCHAR(30)    NOT NULL,
    product_category VARCHAR(30),
    price            NUMERIC(10, 2) NOT NULL
);