package com.iakuil.bf.dao;

import com.iakuil.bf.common.db.CrudMapper;
import com.iakuil.bf.dao.entity.UserRole;
import org.apache.ibatis.annotations.CacheNamespace;

@CacheNamespace
public interface UserRoleMapper extends CrudMapper<UserRole> {
}