package ru.yandex.practicum.shopping.cart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.yandex.practicum.shopping.cart.dal.model.ShoppingCart;
import ru.yandex.practicum.shopping.dto.cart.ShoppingCartDto;

/**
 * @author Andrew Vilkov
 * @created 14.07.2026 - 18:22
 * @project plus-smart-home-tech
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMapper {

    ShoppingCartDto map(ShoppingCart cart);
}
