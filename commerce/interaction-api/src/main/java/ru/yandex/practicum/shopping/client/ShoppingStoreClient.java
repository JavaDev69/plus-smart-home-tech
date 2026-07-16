package ru.yandex.practicum.shopping.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.shopping.client.config.FeignClientConfig;
import ru.yandex.practicum.shopping.dto.cart.SetProductQuantityStateRequest;
import ru.yandex.practicum.shopping.dto.shop.PageProductDto;
import ru.yandex.practicum.shopping.dto.shop.ProductCategory;
import ru.yandex.practicum.shopping.dto.shop.ProductDto;

import java.util.List;
import java.util.UUID;

/**
 * @author Andrew Vilkov
 * @created 16.07.2026 - 12:45
 * @project plus-smart-home-tech
 */
@FeignClient(
        name = "shopping-store",
        path = "/api/v1/shopping-store",
        configuration = FeignClientConfig.class
)
public interface ShoppingStoreClient {
    @GetMapping
    PageProductDto getProducts(
            @RequestParam ProductCategory category,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam List<String> sort);

    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable UUID productId);

    @PutMapping
    ProductDto addProduct(@RequestBody ProductDto productDto);

    @PostMapping
    ProductDto update(@RequestBody ProductDto updProductDto);

    @PostMapping("/quantityState")
    boolean setProductState(@RequestBody SetProductQuantityStateRequest request);

    @PostMapping("/removeProductFromStore")
    boolean remove(@RequestBody UUID productId);
}
