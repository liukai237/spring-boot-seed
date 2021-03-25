package com.iakuil.bf.service.converter;

import com.iakuil.bf.common.BaseConverter;
import com.iakuil.bf.dao.entity.Notify;
import com.iakuil.bf.service.dto.NotifyDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NotifyConverter extends BaseConverter<Notify, NotifyDto> {
    NotifyConverter INSTANCE = Mappers.getMapper(NotifyConverter.class);
}