package ru.yandex.practicum.shopping.dto;

import lombok.Builder;

/**
 * @author Andrew Vilkov
 * @created 10.07.2026 - 17:58
 * @project plus-smart-home-tech
 */
@Builder
public record SortObject(
        String direction,
        String nullHandling,
        Boolean ascending,
        String property,
        Boolean ignoreCase
) {
}
