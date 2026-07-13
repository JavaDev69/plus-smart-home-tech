package ru.yandex.practicum.shopping.store.service.impl;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.shopping.dto.ProductCategory;
import ru.yandex.practicum.shopping.dto.ProductState;
import ru.yandex.practicum.shopping.dto.QuantityState;
import ru.yandex.practicum.shopping.store.dal.model.Product;
import ru.yandex.practicum.shopping.store.dal.repository.ProductRepository;
import ru.yandex.practicum.shopping.store.service.ProductService;

import java.util.UUID;

/**
 * @author Andrew Vilkov
 * @created 11.07.2026 - 19:30
 * @project plus-smart-home-tech
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    @Override
    public Page<Product> getProducts(ProductCategory category, Pageable pageable) {
        log.debug("getProducts({})", category);

        return repository.findAll(pageable);
    }

    @Override
    public Product getProduct(UUID id) {
        log.debug("Get product with id {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.debug("Product with id '{}' not found", id);
                    return new NotFoundException("Product with id '%s' not found".formatted(id));
                });
    }

    @Transactional
    @Override
    public Product save(Product product) {
        product.setId(null);
        log.debug("Saving product: '{}'", product);
        Product saved = repository.save(product);
        log.debug("Saved product: '{}'", saved);
        return saved;
    }

    @Transactional
    @Override
    public Product update(Product product) {
        if (!repository.existsById(product.getId())) {
            throw new NotFoundException("Product with id '%s' not found".formatted(product.getId()));
        }
        log.debug("Updating product: '{}'", product);
        Product updated = repository.save(product);
        log.debug("Updated product: '{}'", updated);
        return updated;
    }

    @Transactional
    @Override
    public boolean remove(UUID productId) {
        log.debug("Removing product with id {}", productId);
        Product product = getProduct(productId);
        try {
            product.setProductState(ProductState.DEACTIVATE);
            repository.save(product);
            log.debug("Removed product with id {}", productId);
            return true;
        } catch (Exception e) {
            log.error("Cannot remove product with id {}", productId, e);
        }
        return false;
    }

    @Transactional
    @Override
    public boolean setState(UUID id, QuantityState quantityState) {
        log.debug("Setting state for product with id '{}' = '{}'", id, quantityState);
        Product product = getProduct(id);
        try {
            product.setQuantityState(quantityState);
            repository.save(product);
            log.debug("The state for product with id '{}' has been set", id);
            return true;
        } catch (Exception e) {
            log.error("Cannot set state for product with id '{}'", id, e);
        }
        return false;
    }
}
