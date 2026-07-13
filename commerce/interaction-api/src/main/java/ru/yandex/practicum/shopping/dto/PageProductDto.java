package ru.yandex.practicum.shopping.dto;

import lombok.Builder;

import java.util.List;

/**
 * @author Andrew Vilkov
 * @created 10.07.2026 - 17:51
 * @project plus-smart-home-tech
 */
@Builder
public record PageProductDto(
        Long totalElements,
        Integer totalPages,
        Boolean first,
        Boolean last,
        Integer size,
        List<ProductDto> content,
        Integer number,
        List<SortObject> sort,
        Integer numberOfElements,
        PageableObject pageable,
        Boolean empty
) {
}
