package ru.yandex.practicum.shopping.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Andrew Vilkov
 * @created 14.07.2026 - 13:23
 * @project plus-smart-home-tech
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoProductsInShoppingCartException extends RuntimeException {
    public NoProductsInShoppingCartException(String message) {
        super(message);
    }
}
