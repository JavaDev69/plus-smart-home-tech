package ru.yandex.practicum.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.shopping.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.shopping.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.shopping.dto.warehouse.AddressDto;
import ru.yandex.practicum.shopping.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.shopping.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.warehouse.mapper.WarehouseMapper;
import ru.yandex.practicum.warehouse.service.AddressService;
import ru.yandex.practicum.warehouse.service.WarehouseService;

/**
 * @author Andrew Vilkov
 * @created 15.07.2026 - 11:52
 * @project plus-smart-home-tech
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
@Validated
@RestController
public class WarehouseController {
    private final AddressService addressService;
    private final WarehouseService service;
    private final WarehouseMapper mapper;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void addProduct(@Valid @RequestBody NewProductInWarehouseRequest request) {
        log.debug("Adding product description in warehouse request: {}", request);
        service.addProduct(mapper.map(request));
        log.debug("Added product in warehouse request: {}", request);
    }

    @PostMapping("/check")
    public BookedProductsDto check(@Valid @RequestBody ShoppingCartDto cartDto) {
        log.debug("Checking cart in warehouse request: {}", cartDto);
        return service.check(cartDto);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void add(@Valid @RequestBody AddProductToWarehouseRequest request) {
        log.debug("Adding product quantity in warehouse request: {}", request);
        service.add(request);
        log.debug("Added product quantity in warehouse request: {}", request);
    }

    @GetMapping("/address")
    public AddressDto getAddress() {
        log.debug("Getting address in warehouse request");
        return addressService.getAddress();
    }
}
