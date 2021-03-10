package com.iakuil.bf.dao;

import com.iakuil.bf.common.db.CrudMapper;
import com.iakuil.bf.dao.entity.Dict;
import org.apache.ibatis.annotations.CacheNamespace;

import java.util.List;

/**
 * 字典表
 *
 * <p>数据字典极少更新，所以采用表级缓存。
 */
@CacheNamespace(implementation = org.mybatis.caches.caffeine.CaffeineCache.class)
public interface DictMapper extends CrudMapper<Dict> {
    List<Dict> listType();
}
