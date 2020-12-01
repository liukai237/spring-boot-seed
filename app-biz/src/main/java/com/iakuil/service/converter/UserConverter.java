package com.iakuil.service.converter;

import com.iakuil.common.BaseConverter;
import com.iakuil.dao.entity.User;
import com.iakuil.service.dto.UserDetailDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserConverter extends BaseConverter<User, UserDetailDto> {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);
}