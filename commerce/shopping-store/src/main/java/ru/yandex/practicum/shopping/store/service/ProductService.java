package ru.yandex.practicum.shopping.store.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.shopping.dto.shop.ProductCategory;
import ru.yandex.practicum.shopping.dto.shop.QuantityState;
import ru.yandex.practicum.shopping.store.dal.model.Product;

import java.util.UUID;

/**
 * @author Andrew Vilkov
 * @created 11.07.2026 - 19:29
 * @project plus-smart-home-tech
 */
public interface ProductService {
    Page<Product> getProducts(ProductCategory category, Pageable pageable);

    Product getProduct(UUID id);

    Product save(Product product);

    Product update(Product product);

    boolean remove(UUID productId);

    boolean setState(@NotNull UUID uuid, @NotNull QuantityState quantityState);
}
