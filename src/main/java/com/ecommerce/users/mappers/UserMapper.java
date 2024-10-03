package com.ecommerce.users.mappers;

import com.ecommerce.users.dto.UserDto;
import com.ecommerce.users.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(UserEntity userEntity);

    UserEntity toEntity(UserDto userDto);
}
