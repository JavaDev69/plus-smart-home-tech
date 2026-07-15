package ru.yandex.practicum.shopping.cart.service;

import ru.yandex.practicum.shopping.cart.dal.model.ShoppingCart;
import ru.yandex.practicum.shopping.dto.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Andrew Vilkov
 * @created 14.07.2026 - 18:17
 * @project plus-smart-home-tech
 */
public interface ShoppingCartService {
    ShoppingCart getShoppingCart(String username);

    void deactivateShoppingCart(String username);

    ShoppingCart removeProductsFromCart(String username, List<UUID> productIds);

    ShoppingCart addToShoppingCart(String username, Map<UUID, Long> items);

    ShoppingCart changeProductQuantity(String username, ChangeProductQuantityRequest request);
}
