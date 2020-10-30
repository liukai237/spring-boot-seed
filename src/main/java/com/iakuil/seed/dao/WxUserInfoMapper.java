package com.iakuil.seed.dao;

import com.iakuil.seed.common.CrudMapper;
import com.iakuil.seed.entity.WxUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WxUserInfoMapper extends CrudMapper<WxUserInfo> {
}
