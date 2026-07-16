package ru.yandex.practicum.shopping.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.shopping.client.config.FeignClientConfig;
import ru.yandex.practicum.shopping.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.shopping.dto.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Andrew Vilkov
 * @created 16.07.2026 - 12:39
 * @project plus-smart-home-tech
 */
@FeignClient(
        name = "shopping-cart",
        path = "/api/v1/shopping-cart",
        configuration = FeignClientConfig.class
)
public interface ShoppingCartClient {
    @GetMapping
    ShoppingCartDto getShoppingCart(@RequestParam String username);

    @PutMapping
    ShoppingCartDto addItemsToShoppingCart(@RequestParam String username,
                                           @RequestBody Map<UUID, Long> items);

    @DeleteMapping
    void deactivateShoppingCart(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto removeItemFromCart(@RequestParam String username,
                                       @RequestBody List<UUID> productIds);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeProductQuantity(@RequestParam String username,
                                          @RequestBody ChangeProductQuantityRequest request);
}
