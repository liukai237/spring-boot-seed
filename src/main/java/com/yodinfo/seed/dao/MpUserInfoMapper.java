package com.yodinfo.seed.dao;

import com.yodinfo.seed.common.CrudMapper;
import com.yodinfo.seed.domain.MpUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MpUserInfoMapper extends CrudMapper<MpUserInfo> {
}
