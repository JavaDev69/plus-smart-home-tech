package ru.yandex.practicum.shopping.cart.dal.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.shopping.dto.ShoppingCartState;

import java.util.Map;
import java.util.UUID;

/**
 * @author Andrew Vilkov
 * @created 14.07.2026 - 14:04
 * @project plus-smart-home-tech
 */
@Builder
@Getter
@Setter
@ToString
@Table(name = "shopping_carts")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID shoppingCartId;
    @Column(unique = true, nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    private ShoppingCartState state;

    @ElementCollection
    @CollectionTable(name = "cart_product", joinColumns = @JoinColumn(name = "shopping_cart_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "product_count")
    private Map<UUID, Long> products;
}
