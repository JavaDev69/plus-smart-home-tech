package ru.yandex.practicum.shopping.cart.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.shopping.cart.dal.model.ShoppingCart;
import ru.yandex.practicum.shopping.dto.ShoppingCartState;

import java.util.List;

/**
 * @author Andrew Vilkov
 * @created 14.07.2026 - 18:24
 * @project plus-smart-home-tech
 */
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    boolean existsByUsername(String username);

    ShoppingCart getByUsername(String username);

    boolean existsByUsernameAndState(String username, ShoppingCartState state);

    List<ShoppingCart> getByUsernameAndState(String username, ShoppingCartState state);

    ShoppingCart getOneByUsernameAndState(String username, ShoppingCartState state);
}
