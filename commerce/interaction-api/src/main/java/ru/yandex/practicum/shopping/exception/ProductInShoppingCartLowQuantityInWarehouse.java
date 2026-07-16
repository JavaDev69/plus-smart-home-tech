package ru.yandex.practicum.shopping.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Andrew Vilkov
 * @created 15.07.2026 - 12:10
 * @project plus-smart-home-tech
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductInShoppingCartLowQuantityInWarehouse extends RuntimeException {
    public ProductInShoppingCartLowQuantityInWarehouse(String message) {
        super(message);
    }
}
