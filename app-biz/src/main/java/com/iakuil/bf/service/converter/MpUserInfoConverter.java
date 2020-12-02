package com.iakuil.bf.service.converter;

import com.iakuil.bf.common.BaseConverter;
import com.iakuil.bf.dao.entity.MpUserInfo;
import com.iakuil.bf.service.dto.wx.MpUserInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MpUserInfoConverter extends BaseConverter<MpUserInfo, MpUserInfoDTO> {
    MpUserInfoConverter INSTANCE = Mappers.getMapper(MpUserInfoConverter.class);
}