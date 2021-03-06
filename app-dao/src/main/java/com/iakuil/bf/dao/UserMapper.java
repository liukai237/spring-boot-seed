package com.iakuil.bf.dao;

import com.iakuil.bf.common.db.CrudMapper;
import com.iakuil.bf.dao.entity.User;

public interface UserMapper extends CrudMapper<User> {
    User selectByIdentity(String identity);
}