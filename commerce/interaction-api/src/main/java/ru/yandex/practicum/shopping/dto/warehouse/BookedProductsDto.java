package ru.yandex.practicum.shopping.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

/**
 * @author Andrew Vilkov
 * @created 15.07.2026 - 12:09
 * @project plus-smart-home-tech
 */
@Builder
public record BookedProductsDto(
        @NotNull @Positive Double deliveryWeight,
        @NotNull @Positive Double deliveryVolume,
        @NotNull Boolean fragile
) {
}
