CREATE TABLE IF NOT EXISTS products
(
    id               UUID PRIMARY KEY,
    product_name     VARCHAR(255)   NOT NULL,
    description      VARCHAR(255)   NOT NULL,
    image_src        VARCHAR(255),
    quantity_state   VARCHAR(255)   NOT NULL,
    product_state    VARCHAR(255)   NOT NULL,
    product_category VARCHAR(255),
    price            NUMERIC(19, 2) NOT NULL
);