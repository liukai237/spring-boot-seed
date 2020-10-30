package com.iakuil.seed.converter;

import com.iakuil.seed.entity.User;
import com.iakuil.seed.dto.UserAddParam;
import com.iakuil.seed.dto.UserDetailDto;
import com.iakuil.seed.dto.UserEditParam;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    @Mappings({
            @Mapping(target = "uid", source = "userId"),
            @Mapping(target = "regTime", source = "createTime")
    })
    @Named("toDto")
    UserDetailDto toDto(User user);

    @Mapping(target = "passwdHash", source = "password")
    User toEntity(UserAddParam info);

    @Mappings({
            @Mapping(target = "userId", source = "uid")
    })
    User toEntity(UserEditParam info);

    @IterableMapping(qualifiedByName = "toDto")
    List<UserDetailDto> toDtoList(List<User> entities);
}