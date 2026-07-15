package ru.yandex.practicum.shopping.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.util.UUID;

/**
 * @author Andrew Vilkov
 * @created 14.07.2026 - 13:24
 * @project plus-smart-home-tech
 */
@Builder
public record ChangeProductQuantityRequest(
        @NotNull UUID productId,
        @NotNull @PositiveOrZero Long newQuantity
) {
}
