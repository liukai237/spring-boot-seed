package com.iakuil.seed.converter;

import com.iakuil.seed.common.BaseConverter;
import com.iakuil.seed.entity.MpUserInfo;
import com.iakuil.seed.dto.wx.MpUserInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MpUserInfoConverter extends BaseConverter<MpUserInfo, MpUserInfoDTO> {
    MpUserInfoConverter INSTANCE = Mappers.getMapper(MpUserInfoConverter.class);
}