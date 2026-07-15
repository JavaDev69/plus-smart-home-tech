package ru.yandex.practicum.shopping.cart.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.shopping.cart.dal.model.ShoppingCart;
import ru.yandex.practicum.shopping.cart.dal.repository.ShoppingCartRepository;
import ru.yandex.practicum.shopping.cart.service.ShoppingCartService;
import ru.yandex.practicum.shopping.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.shopping.dto.ShoppingCartState;
import ru.yandex.practicum.shopping.exception.NoProductsInShoppingCartException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Andrew Vilkov
 * @created 14.07.2026 - 18:18
 * @project plus-smart-home-tech
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository repository;

    @Transactional
    @Override
    public ShoppingCart getShoppingCart(String username) {
        return getOrCreateCart(username);
    }

    @Transactional
    @Override
    public void deactivateShoppingCart(String username) {
        ShoppingCart cart = repository.getOneByUsernameAndState(username, ShoppingCartState.ACTIVE);
        cart.setState(ShoppingCartState.DEACTIVATE);
        repository.save(cart);
    }

    @Transactional
    @Override
    public ShoppingCart removeProductsFromCart(String username, List<UUID> productIds) {
        ShoppingCart cart = repository.getOneByUsernameAndState(username, ShoppingCartState.ACTIVE);
        if (!cart.getProducts().keySet().containsAll(productIds)) {
            throw new NoProductsInShoppingCartException("No found products by ids: " + productIds);
        }
        productIds.forEach(cart.getProducts()::remove);
        return repository.save(cart);
    }

    @Transactional
    @Override
    public ShoppingCart addToShoppingCart(String username, Map<UUID, Long> items) {
        ShoppingCart shoppingCart = getOrCreateCart(username);
        shoppingCart.getProducts().putAll(items);
        repository.save(shoppingCart);
        return shoppingCart;
    }

    @Override
    public ShoppingCart changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        ShoppingCart shoppingCart = getOrCreateCart(username);
        if (!shoppingCart.getProducts().containsKey(request.productId())) {
            throw new NoProductsInShoppingCartException("No found product by id: " + request.productId());
        }

        shoppingCart.getProducts().replace(request.productId(), request.newQuantity());
        repository.save(shoppingCart);
        return shoppingCart;
    }

    private ShoppingCart getOrCreateCart(String username) {
        if (repository.existsByUsernameAndState(username, ShoppingCartState.ACTIVE)) {
            return repository.getOneByUsernameAndState(username, ShoppingCartState.ACTIVE);
        }
        return repository.save(newShoppingCartForUser(username));
    }

    private ShoppingCart newShoppingCartForUser(String username) {
        return ShoppingCart.builder()
                .username(username)
                .state(ShoppingCartState.ACTIVE)
                .products(new HashMap<>())
                .build();
    }
}
