package com.yodinfo.seed.converter;

import com.yodinfo.seed.domain.User;
import com.yodinfo.seed.dto.UserAddDto;
import com.yodinfo.seed.dto.UserDetailDto;
import com.yodinfo.seed.dto.UserEditDto;
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
    User toEntity(UserAddDto info);

    @Mappings({
            @Mapping(target = "userId", source = "uid")
    })
    User toEntity(UserEditDto info);

    @IterableMapping(qualifiedByName = "toDto")
    List<UserDetailDto> toDtoList(List<User> entities);
}