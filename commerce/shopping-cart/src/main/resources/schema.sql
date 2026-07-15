CREATE TABLE IF NOT EXISTS shopping_carts
(
    shopping_cart_id
    UUID
    PRIMARY
    KEY,
    username
    VARCHAR
(
    255
) NOT NULL,
    state VARCHAR
(
    255
) NOT NULL,
    CONSTRAINT UNIQ_CART_USER UNIQUE
(
    shopping_cart_id,
    username
)
    );

CREATE TABLE IF NOT EXISTS cart_product
(
    shopping_cart_id
    UUID,
    product_id
    UUID,
    product_count
    BIGINT
    NOT
    NULL,
    CONSTRAINT
    FK_SHOPPING_CART_ID
    FOREIGN
    KEY
(
    shopping_cart_id
) REFERENCES shopping_carts
(
    shopping_cart_id
),
    CONSTRAINT PK_CART_PRODUCT PRIMARY KEY
(
    shopping_cart_id,
    product_id
)
    );