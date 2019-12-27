package com.yodinfo.seed.converter;

import com.yodinfo.seed.domain.User;
import com.yodinfo.seed.dto.BasicUserInfo;
import com.yodinfo.seed.dto.UserRegInfo;
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
    BasicUserInfo toDto(User user);

    @Mapping(target = "passwdHash", source = "password")
    User toEntity(UserRegInfo info);

    @Mappings({
            @Mapping(target = "userId", source = "uid")
    })
    User toEntity(BasicUserInfo info);

    @IterableMapping(qualifiedByName = "toDto")
    List<BasicUserInfo> toDtoList(List<User> quotes);
}