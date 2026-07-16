package ru.yandex.practicum.warehouse.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.warehouse.dal.model.Product;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByProductIdIsIn(Collection<UUID> productIds);

    Optional<Product> findOneByProductId(UUID productId);

    boolean existsByProductId(UUID productId);
}