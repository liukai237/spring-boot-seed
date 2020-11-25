package com.iakuil.seed.dao;

import com.iakuil.seed.common.CrudMapper;
import com.iakuil.seed.common.QueryBase;
import com.iakuil.seed.entity.User;
import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@CacheNamespace
public interface UserMapper extends CrudMapper<User> {
    User selectByOpenId(String identity);

    int deleteOpenId(String tel);

    User selectByIdentity(String identity);

    List<User> selectByCondition(QueryBase condition);
}