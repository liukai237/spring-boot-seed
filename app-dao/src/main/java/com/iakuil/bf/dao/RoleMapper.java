package com.iakuil.bf.dao;

import com.iakuil.bf.common.db.CrudMapper;
import com.iakuil.bf.dao.entity.Role;
import org.apache.ibatis.annotations.CacheNamespace;

@CacheNamespace
public interface RoleMapper extends CrudMapper<Role> {
}
