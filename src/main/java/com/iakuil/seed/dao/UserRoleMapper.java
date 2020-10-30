package com.iakuil.seed.dao;

import com.iakuil.seed.entity.UserRole;
import com.iakuil.seed.common.CrudMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@CacheNamespace
@Repository
public interface UserRoleMapper extends CrudMapper<UserRole> {
}