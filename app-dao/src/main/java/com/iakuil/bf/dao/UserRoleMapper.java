package com.iakuil.bf.dao;

import com.iakuil.bf.common.db.CrudMapper;
import com.iakuil.bf.dao.entity.UserRole;
import org.apache.ibatis.annotations.CacheNamespace;
import org.mybatis.caches.caffeine.CaffeineCache;

@CacheNamespace(implementation = CaffeineCache.class)
public interface UserRoleMapper extends CrudMapper<UserRole> {
}