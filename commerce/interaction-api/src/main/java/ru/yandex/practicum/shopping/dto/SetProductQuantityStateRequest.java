package ru.yandex.practicum.shopping.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

/**
 * @author Andrew Vilkov
 * @created 11.07.2026 - 18:34
 * @project plus-smart-home-tech
 */
@Builder
public record SetProductQuantityStateRequest(
        @NotNull UUID productId,
        @NotNull QuantityState quantityState
) {

}
