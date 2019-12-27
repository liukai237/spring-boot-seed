package com.yodinfo.seed.converter;

import com.yodinfo.seed.domain.UserInfo;
import com.yodinfo.seed.dto.UserInfoDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.List;


@Mapper(componentModel = "spring")
public interface UserInfoConverter {

    UserInfoConverter INSTANCE = Mappers.getMapper(UserInfoConverter.class);

    @Mappings({
            @Mapping(target = "subscribeTime", ignore = true),
    })
    @Named("toDto")
    UserInfoDto toDto(UserInfo entity);

    @IterableMapping(qualifiedByName = "toDto")
    List<UserInfoDto> toDtoList(List<UserInfo> entities);

    @Mappings({
            @Mapping(target = "subscribeTime", ignore = true),
    })
    @Named("toEntity")
    UserInfo toEntity(UserInfoDto dto);

    @AfterMapping
    default void handleSomething(UserInfoDto dto, @MappingTarget UserInfo entity) {
        Long subscribeTime = dto.getSubscribeTime();
        if (subscribeTime != null) {
            entity.setSubscribeTime(new Date(subscribeTime * 1000));
        }
    }

//    @AfterMapping
//    default void handleSomething(UserInfo entity, @MappingTarget UserInfoDto dto) {
//        Date subscribeTime = entity.getSubscribeTime();
//        if (subscribeTime != null) {
//            dto.setSubscribe(subscribeTime.getTime()/ 1000);
//        }
//    }
}