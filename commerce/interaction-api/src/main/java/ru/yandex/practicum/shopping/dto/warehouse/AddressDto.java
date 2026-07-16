package ru.yandex.practicum.shopping.dto.warehouse;

import lombok.Builder;

/**
 * @author Andrew Vilkov
 * @created 15.07.2026 - 12:12
 * @project plus-smart-home-tech
 */
@Builder
public record AddressDto(
        String country,
        String city,
        String street,
        String house,
        String flat
) {
}
