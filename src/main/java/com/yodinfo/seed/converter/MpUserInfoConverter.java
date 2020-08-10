package com.yodinfo.seed.converter;

import com.yodinfo.seed.common.BaseConverter;
import com.yodinfo.seed.domain.MpUserInfo;
import com.yodinfo.seed.dto.wx.MpUserInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MpUserInfoConverter extends BaseConverter<MpUserInfo, MpUserInfoDTO> {
    MpUserInfoConverter INSTANCE = Mappers.getMapper(MpUserInfoConverter.class);
}