package com.yodinfo.seed.dao;

import com.yodinfo.seed.common.BasicMapper;
import com.yodinfo.seed.domain.WxUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WxUserInfoMapper extends BasicMapper<WxUserInfo> {
}
