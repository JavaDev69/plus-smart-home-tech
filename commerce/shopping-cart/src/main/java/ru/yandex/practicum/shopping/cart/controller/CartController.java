package ru.yandex.practicum.shopping.cart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.shopping.cart.dal.model.ShoppingCart;
import ru.yandex.practicum.shopping.cart.mapper.CartMapper;
import ru.yandex.practicum.shopping.cart.service.ShoppingCartService;
import ru.yandex.practicum.shopping.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.shopping.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Andrew Vilkov
 * @created 14.07.2026 - 13:13
 * @project plus-smart-home-tech
 */
@Slf4j
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
@RestController
@Validated
public class CartController {
    private final ShoppingCartService service;

    private final CartMapper mapper;

    @GetMapping
    public ShoppingCartDto getShoppingCart(@Valid @NotBlank @RequestParam String username) {
        log.debug("Get shopping cart for username {}", username);
        ShoppingCart cart = service.getShoppingCart(username);
        log.debug("Found shopping cart for username {} is {}", username, cart);
        return mapper.map(cart);
    }


    @PutMapping
    public ShoppingCartDto addItemsToShoppingCart(@RequestParam String username,
                                                  @RequestBody Map<UUID, Long> items) {
        log.debug("Add items {} to cart for username {}", items, username);
        ShoppingCart cart = service.addToShoppingCart(username, items);
        log.debug("Updated shopping cart for username {} is {}", username, cart);
        return mapper.map(cart);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deactivateShoppingCart(@RequestParam String username) {
        log.debug("Deactivate shopping cart for username {}", username);
        service.deactivateShoppingCart(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeItemFromCart(@RequestParam String username,
                                              @RequestBody List<UUID> productIds) {
        log.debug("Remove items {} from cart for username {}", productIds, username);
        return mapper.map(service.removeProductsFromCart(username, productIds));
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantity(@RequestParam String username,
                                                 @Valid @RequestBody ChangeProductQuantityRequest request) {
        log.debug("Change product quantity for username {}", username);
        return mapper.map(service.changeProductQuantity(username, request));
    }
}
