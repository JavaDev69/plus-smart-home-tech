package ru.yandex.practicum.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.shopping.dto.warehouse.AddressDto;
import ru.yandex.practicum.shopping.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.warehouse.dal.model.Product;

/**
 * @author Andrew Vilkov
 * @created 15.07.2026 - 14:05
 * @project plus-smart-home-tech
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper {
    @Mapping(target = "country", source = ".")
    @Mapping(target = "city", source = ".")
    @Mapping(target = "street", source = ".")
    @Mapping(target = "house", source = ".")
    @Mapping(target = "flat", source = ".")
    AddressDto mapAddress(String currentAddress);

    @Mapping(target = "width", source = "dimension.width")
    @Mapping(target = "height", source = "dimension.height")
    @Mapping(target = "depth", source = "dimension.depth")
    @Mapping(target = "quantity", constant = "0L")
    Product map(NewProductInWarehouseRequest request);
}
