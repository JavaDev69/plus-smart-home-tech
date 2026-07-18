package ru.yandex.practicum.warehouse.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.shopping.exception.ApiErrorResponse;
import ru.yandex.practicum.shopping.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.shopping.exception.SpecifiedProductAlreadyInWarehouseException;

import java.time.Instant;

/**
 * @author Andrew Vilkov
 * @created 16.07.2026 - 17:08
 * @project plus-smart-home-tech
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    public ResponseEntity<ApiErrorResponse> handleException(
            NoSpecifiedProductInWarehouseException ex,
            HttpServletRequest request
    ) {
        log.warn("NoSpecifiedProductInWarehouseException error while processing request {}",
                request.getRequestURI(),
                ex);

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    public ResponseEntity<ApiErrorResponse> handleException(
            SpecifiedProductAlreadyInWarehouseException ex,
            HttpServletRequest request
    ) {
        log.warn("SpecifiedProductAlreadyInWarehouseException error while processing request {}",
                request.getRequestURI(),
                ex);

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request
    ) {
        log.warn("Unexpected error while processing request {}", request.getRequestURI(), exception);

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Произошла внутренняя ошибка сервера",
                request
        );
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(status).body(response);
    }
}
