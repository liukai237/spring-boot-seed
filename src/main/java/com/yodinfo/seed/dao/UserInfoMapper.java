package com.yodinfo.seed.dao;

import com.yodinfo.seed.common.CommonMapper;
import com.yodinfo.seed.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends CommonMapper<UserInfo> {
}
