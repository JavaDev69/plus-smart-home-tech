package ru.yandex.practicum.shopping.store.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.shopping.dto.shop.PageProductDto;
import ru.yandex.practicum.shopping.dto.shop.ProductCategory;
import ru.yandex.practicum.shopping.dto.shop.ProductDto;
import ru.yandex.practicum.shopping.dto.shop.QuantityState;
import ru.yandex.practicum.shopping.store.dal.model.Product;
import ru.yandex.practicum.shopping.store.mapper.ProductMapper;
import ru.yandex.practicum.shopping.store.service.ProductService;

import java.util.UUID;

/**
 * @author Andrew Vilkov
 * @created 10.07.2026 - 17:47
 * @project plus-smart-home-tech
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/shopping-store")
@RestController()
public class ShoppingController {
    private final ProductMapper mapper;
    private final ProductService service;

    @GetMapping
    public PageProductDto getProducts(
            @RequestParam(name = "category") ProductCategory category,
            @PageableDefault(
                    page = 0,
                    size = 20,
                    sort = "productName",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        return mapper.map(service.getProducts(category, pageable));
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable UUID productId) {
        return mapper.map(service.getProduct(productId));
    }

    @PutMapping
    public ProductDto addProduct(@Valid @RequestBody ProductDto productDto) {
        Product product = mapper.map(productDto);
        return mapper.map(service.save(product));
    }

    @PostMapping
    public ProductDto update(@Valid @RequestBody ProductDto updProductDto) {
        log.debug("Request for update product: {}", updProductDto);
        if (updProductDto.productId() == null) {
            throw new ValidationException("productId is null");
        }

        Product product = mapper.map(updProductDto);
        return mapper.map(service.update(product));
    }

    @PostMapping("/quantityState")
    public boolean setProductState(@RequestParam UUID productId, @RequestParam QuantityState quantityState) {
        log.debug("Request for set product '{}' quantity state: {}", productId, quantityState);
        return service.setState(productId, quantityState);
    }

    @PostMapping("/removeProductFromStore")
    public boolean remove(@Valid @NotNull @RequestBody UUID productId) {
        log.debug("Request for remove product from store: {}", productId);
        return service.remove(productId);
    }
}