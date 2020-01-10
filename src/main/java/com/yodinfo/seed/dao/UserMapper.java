package com.yodinfo.seed.dao;

import com.yodinfo.seed.common.BasicMapper;
import com.yodinfo.seed.domain.User;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@CacheNamespace
@Repository
public interface UserMapper extends BasicMapper<User> {
    List<User> selectByIdentity(String identity);
}