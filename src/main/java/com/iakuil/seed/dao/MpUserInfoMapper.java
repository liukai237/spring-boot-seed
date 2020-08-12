package com.iakuil.seed.dao;

import com.iakuil.seed.common.CrudMapper;
import com.iakuil.seed.domain.MpUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MpUserInfoMapper extends CrudMapper<MpUserInfo> {
}
