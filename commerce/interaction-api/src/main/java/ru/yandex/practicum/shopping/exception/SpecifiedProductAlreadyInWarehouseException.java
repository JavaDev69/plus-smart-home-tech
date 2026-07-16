package ru.yandex.practicum.shopping.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Andrew Vilkov
 * @created 15.07.2026 - 12:02
 * @project plus-smart-home-tech
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SpecifiedProductAlreadyInWarehouseException extends RuntimeException {
    public SpecifiedProductAlreadyInWarehouseException(String message) {
        super(message);
    }
}
