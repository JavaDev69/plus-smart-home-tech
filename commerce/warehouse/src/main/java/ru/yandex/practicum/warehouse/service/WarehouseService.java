package ru.yandex.practicum.warehouse.service;

import ru.yandex.practicum.shopping.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.shopping.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.shopping.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.warehouse.dal.model.Product;

/**
 * @author Andrew Vilkov
 * @created 15.07.2026 - 11:52
 * @project plus-smart-home-tech
 */
public interface WarehouseService {
    void addProduct(Product product);

    BookedProductsDto check(ShoppingCartDto cartDto);

    void add(AddProductToWarehouseRequest request);
}
