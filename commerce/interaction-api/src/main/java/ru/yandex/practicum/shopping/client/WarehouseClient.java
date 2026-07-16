package ru.yandex.practicum.shopping.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.shopping.client.config.FeignClientConfig;
import ru.yandex.practicum.shopping.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.shopping.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.shopping.dto.warehouse.AddressDto;
import ru.yandex.practicum.shopping.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.shopping.dto.warehouse.NewProductInWarehouseRequest;

/**
 * @author Andrew Vilkov
 * @created 15.07.2026 - 17:27
 * @project plus-smart-home-tech
 */
@FeignClient(
        name = "warehouse",
        path = "/api/v1/warehouse",
        configuration = FeignClientConfig.class
)
public interface WarehouseClient {
    @PutMapping
    void addProduct(@RequestBody NewProductInWarehouseRequest request);

    @PostMapping("/check")
    BookedProductsDto check(@RequestBody ShoppingCartDto cartDto);

    @PostMapping("/add")
    void add(@RequestBody AddProductToWarehouseRequest request);

    @GetMapping("/address")
    AddressDto getAddress();
}
