package com.iakuil.seed.dao;

import com.iakuil.seed.domain.User;
import com.iakuil.seed.common.CrudMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@CacheNamespace
@Repository
public interface UserMapper extends CrudMapper<User> {
    List<User> selectByIdentity(String identity);
    
    List<User> selectByCondition(Map<String, Object> condition);
}