package com.yodinfo.seed.dao;

import com.yodinfo.seed.domain.User;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@CacheNamespace
public interface UserMapper extends CommonMapper<User> {
}