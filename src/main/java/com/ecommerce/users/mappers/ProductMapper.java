package com.ecommerce.users.mappers;


import com.ecommerce.users.dto.ProductDto;
import com.ecommerce.users.entities.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(ProductEntity productEntity);

    ProductEntity toEntity(ProductDto productDto);
}
