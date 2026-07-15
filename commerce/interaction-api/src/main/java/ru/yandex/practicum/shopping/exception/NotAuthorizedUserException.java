package ru.yandex.practicum.shopping.exception;

/**
 * @author Andrew Vilkov
 * @created 14.07.2026 - 13:21
 * @project plus-smart-home-tech
 */
public class NotAuthorizedUserException extends RuntimeException {
    public NotAuthorizedUserException(String message) {
        super(message);
    }
}
