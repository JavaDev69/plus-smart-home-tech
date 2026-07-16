package ru.yandex.practicum.shopping.dto.shop;

import lombok.Builder;

import java.util.List;

/**
 * @author Andrew Vilkov
 * @created 10.07.2026 - 17:58
 * @project plus-smart-home-tech
 */
@Builder
public record PageableObject(
        Long offset,
        List<SortObject> sort,
        Boolean unpaged,
        Boolean paged,
        Integer pageNumber,
        Integer pageSize
) {
}
