package com.yodinfo.seed.dao;

import com.yodinfo.seed.common.CommonMapper;
import com.yodinfo.seed.domain.User;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
@CacheNamespace
public interface UserMapper extends CommonMapper<User> {

    List<User> findByCondition(Map<String, Object> params);

    @Select("select * from t_user")
    List<User> findAll();
}