package com.iakuil.service.converter;

import com.iakuil.common.BaseConverter;
import com.iakuil.dao.entity.MpUserInfo;
import com.iakuil.service.dto.wx.MpUserInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MpUserInfoConverter extends BaseConverter<MpUserInfo, MpUserInfoDTO> {
    MpUserInfoConverter INSTANCE = Mappers.getMapper(MpUserInfoConverter.class);
}