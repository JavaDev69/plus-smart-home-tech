package ru.yandex.practicum.shopping.store.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import ru.yandex.practicum.shopping.dto.shop.PageProductDto;
import ru.yandex.practicum.shopping.dto.shop.ProductDto;
import ru.yandex.practicum.shopping.store.dal.model.Product;

/**
 * @author Andrew Vilkov
 * @created 11.07.2026 - 19:24
 * @project plus-smart-home-tech
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    @Mapping(target = "id", source = "productId")
    Product map(ProductDto productDto);

    @Mapping(target = "productId", source = "id")
    ProductDto map(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProduct(Product updProduct, @MappingTarget Product targetProduct);

    PageProductDto map(Page<Product> products);

}
