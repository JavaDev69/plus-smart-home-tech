package ru.yandex.practicum.shopping.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Andrew Vilkov
 * @created 10.07.2026 - 18:35
 * @project plus-smart-home-tech
 */
@Builder
public record ProductDto(
        UUID productId,

        @NotBlank
        String productName,

        @NotBlank
        String description,

        String imageSrc,

        @NotNull
        QuantityState quantityState,

        @NotNull
        ProductState productState,

        ProductCategory productCategory,

        @Min(1)
        BigDecimal price
) {
}
