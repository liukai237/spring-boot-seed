package com.iakuil.bf.service.converter;

import com.iakuil.bf.common.BaseConverter;
import com.iakuil.bf.dao.entity.User;
import com.iakuil.bf.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserConverter extends BaseConverter<User, UserDto> {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);
}