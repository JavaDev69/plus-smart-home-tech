package ru.yandex.practicum.shopping.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

/**
 * @author Andrew Vilkov
 * @created 15.07.2026 - 12:11
 * @project plus-smart-home-tech
 */
@Builder
public record AddProductToWarehouseRequest(
        @NotNull UUID productId,
        @NotNull @Min(1) Long quantity
) {

}
