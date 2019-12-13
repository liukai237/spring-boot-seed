package com.yodinfo.seed.dao;

import com.yodinfo.seed.common.CommonMapper;
import com.yodinfo.seed.domain.Role;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@CacheNamespace
public interface RoleMapper extends CommonMapper<Role> {
}
