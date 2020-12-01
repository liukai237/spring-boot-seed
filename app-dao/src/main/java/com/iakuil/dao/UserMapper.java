package com.iakuil.dao;

import com.iakuil.common.QueryBase;
import com.iakuil.common.db.CrudMapper;
import com.iakuil.dao.entity.User;
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