package ru.yandex.practicum.shopping.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * @author Andrew Vilkov
 * @created 15.07.2026 - 11:55
 * @project plus-smart-home-tech
 */
@Builder
public record DimensionDto(
        @NotNull @Min(1) Double width,
        @NotNull @Min(1) Double height,
        @NotNull @Min(1) Double depth
) {
}
