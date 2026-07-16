package ru.yandex.practicum.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.shopping.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.shopping.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.shopping.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.shopping.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.shopping.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.shopping.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.warehouse.dal.model.Product;
import ru.yandex.practicum.warehouse.dal.repository.ProductRepository;
import ru.yandex.practicum.warehouse.service.WarehouseService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Andrew Vilkov
 * @created 15.07.2026 - 11:53
 * @project plus-smart-home-tech
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final ProductRepository repository;

    @Transactional
    @Override
    public void addProduct(Product product) {
        if (repository.existsByProductId(product.getProductId())) {
            log.error("Product with id {} already exists", product.getProductId());
            throw new SpecifiedProductAlreadyInWarehouseException("Product already exist for id: " + product.getProductId());
        }
        repository.save(product);
    }

    @Override
    public BookedProductsDto check(ShoppingCartDto cartDto) {
        Set<UUID> productIds = cartDto.products().keySet();
        List<Product> allByProductIds = repository.findAllByProductIdIsIn(productIds);

        checkProductQuantity(allByProductIds, cartDto.products());

        return calculateBookedProducts(allByProductIds, cartDto.products());
    }

    @Transactional
    @Override
    public void add(AddProductToWarehouseRequest request) {
        Product product = repository.findOneByProductId(request.productId())
                .orElseThrow(() -> {
                    log.debug("Product with id {} does not exist", request.productId());
                    return new NoSpecifiedProductInWarehouseException("Product not found with id: " + request.productId());
                });
        product.setQuantity(request.quantity());
        repository.save(product);
    }

    private BookedProductsDto calculateBookedProducts(List<Product> products, Map<UUID, Long> productsFromCart) {
        double deliveryVolume = 0.;
        double deliveryWeight = 0.;
        boolean fragile = false;

        for (Product product : products) {
            Long count = productsFromCart.get(product.getProductId());
            Double volume = product.getHeight() * product.getDepth() * product.getWidth();
            Double weight = product.getWeight();

            deliveryVolume += volume * count;
            deliveryWeight += weight * count;
            fragile = fragile || product.getFragile();
        }

        return BookedProductsDto.builder()
                .deliveryWeight(deliveryWeight)
                .deliveryVolume(deliveryVolume)
                .fragile(fragile)
                .build();
    }

    private void checkProductQuantity(List<Product> products, Map<UUID, Long> productsFromCart) {
        products.forEach(product -> {
            Long expectedQuantity = productsFromCart.get(product.getProductId());
            log.debug("Checking product with id {} quantity: {}, expected {}",
                    product.getProductId(),
                    product.getQuantity(),
                    expectedQuantity);
            if (product.getQuantity() < expectedQuantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Low quantity product with id: "
                        + product.getProductId());
            }
        });
    }
}
