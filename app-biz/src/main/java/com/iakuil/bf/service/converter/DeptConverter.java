package com.iakuil.bf.service.converter;

import com.iakuil.bf.common.BaseConverter;
import com.iakuil.bf.dao.entity.DeptDO;
import com.iakuil.bf.service.dto.DeptDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DeptConverter extends BaseConverter<DeptDO, DeptDto> {
    DeptConverter INSTANCE = Mappers.getMapper(DeptConverter.class);
}