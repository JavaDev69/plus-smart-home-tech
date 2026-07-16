package ru.yandex.practicum.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.shopping.dto.warehouse.AddressDto;
import ru.yandex.practicum.warehouse.mapper.WarehouseMapper;
import ru.yandex.practicum.warehouse.service.AddressService;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Andrew Vilkov
 * @created 15.07.2026 - 17:24
 * @project plus-smart-home-tech
 */
@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {
    private final WarehouseMapper mapper;
    private static final String[] ADDRESSES = new String[]{"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    @Override
    public AddressDto getAddress() {
        return mapper.mapAddress(CURRENT_ADDRESS);
    }
}
