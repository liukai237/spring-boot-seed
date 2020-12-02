package com.iakuil.bf.dao;

import com.iakuil.bf.common.PageQuery;
import com.iakuil.bf.common.db.CrudMapper;
import com.iakuil.bf.dao.entity.User;
import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@CacheNamespace
public interface UserMapper extends CrudMapper<User> {
    User selectByOpenId(String identity);

    int deleteOpenId(String tel);

    User selectByIdentity(String identity);

    List<User> selectByCondition(PageQuery condition);
}