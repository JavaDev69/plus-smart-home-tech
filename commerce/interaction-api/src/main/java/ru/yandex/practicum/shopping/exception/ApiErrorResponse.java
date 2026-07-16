package ru.yandex.practicum.shopping.exception;

import lombok.Builder;

import java.time.Instant;

/**
 * @author Andrew Vilkov
 * @created 16.07.2026 - 17:22
 * @project plus-smart-home-tech
 */
@Builder
public record ApiErrorResponse(
        int status,
        String error,
        String message,
        String path,
        Instant timestamp
) {
}
