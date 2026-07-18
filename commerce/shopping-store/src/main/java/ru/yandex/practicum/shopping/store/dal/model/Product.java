package ru.yandex.practicum.shopping.store.dal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.shopping.dto.shop.ProductCategory;
import ru.yandex.practicum.shopping.dto.shop.ProductState;
import ru.yandex.practicum.shopping.dto.shop.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Andrew Vilkov
 * @created 11.07.2026 - 19:04
 * @project plus-smart-home-tech
 */
@Getter
@Setter
@ToString
@Table(name = "products")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String productName;

    @Column
    private String description;

    @Column
    private String imageSrc;

    @Enumerated(EnumType.STRING)
    private QuantityState quantityState;

    @Enumerated(EnumType.STRING)
    private ProductState productState;

    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}
