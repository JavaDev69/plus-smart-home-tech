package ru.yandex.practicum.shopping.store.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.shopping.store.dal.model.Product;

import java.util.UUID;

/**
 * @author Andrew Vilkov
 * @created 11.07.2026 - 19:03
 * @project plus-smart-home-tech
 */
public interface ProductRepository extends JpaRepository<Product, UUID> {
}
