package com.iakuil.dao;

import com.iakuil.common.db.CrudMapper;
import com.iakuil.dao.entity.Role;
import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Repository;

@Repository
@CacheNamespace
public interface RoleMapper extends CrudMapper<Role> {
}
