package com.iakuil.bf.service.converter;

import com.iakuil.bf.common.BaseConverter;
import com.iakuil.bf.dao.entity.Dict;
import com.iakuil.bf.service.dto.DictDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DictConverter extends BaseConverter<Dict, DictDto> {
    DictConverter INSTANCE = Mappers.getMapper(DictConverter.class);
}