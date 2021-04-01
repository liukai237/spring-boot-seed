package com.iakuil.bf.service.converter;

import com.iakuil.bf.common.BaseConverter;
import com.iakuil.bf.dao.entity.MenuDO;
import com.iakuil.bf.dao.entity.Role;
import com.iakuil.bf.service.dto.MenuDto;
import com.iakuil.bf.service.dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MenuConverter extends BaseConverter<MenuDO, MenuDto> {
    MenuConverter INSTANCE = Mappers.getMapper(MenuConverter.class);
}