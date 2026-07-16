package ru.yandex.practicum.warehouse.dal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID productId;

    @Column
    private Boolean fragile;

    @Column
    private Double width;

    @Column
    private Double height;

    @Column
    private Double depth;

    @Column
    private Double weight;

    @Column
    private Long quantity;
}
