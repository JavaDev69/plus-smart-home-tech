package ru.yandex.practicum.shopping.dto.cart;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Map;
import java.util.UUID;

/**
 * @author Andrew Vilkov
 * @created 14.07.2026 - 13:19
 * @project plus-smart-home-tech
 */
@Builder
public record ShoppingCartDto(
        @NotNull UUID shoppingCartId,
        @NotNull Map<UUID, Long> products
) {
}
