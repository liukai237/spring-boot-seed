package com.yodinfo.seed.converter;

import com.yodinfo.seed.domain.User;
import com.yodinfo.seed.dto.BasicUserInfo;
import com.yodinfo.seed.dto.UserRegInfo;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserConverter {

    @Mappings({
            @Mapping(target = "nickname", source = "username"),
            @Mapping(target = "regTime", source = "createTime")
    })
    @Named("toDto")
    BasicUserInfo toDto(User user);

    User toEntity(UserRegInfo info);

    @Mappings({
            @Mapping(target = "username", source = "nickname"),
    })
    User toEntity(BasicUserInfo info);

    @IterableMapping(qualifiedByName = "toDto")
    List<BasicUserInfo> toDtoList(List<User> quotes);
}