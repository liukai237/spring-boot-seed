package com.yodinfo.seed.common;

import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.Marker;

/**
 * tkMapper基类
 *
 * <p>推荐使用tkMapper提供的CRUD方法，不建议使用{@code Example}等对象查询。</p>
 * @param <T> Entity
 */
public interface BasicMapper<T> extends BaseMapper<T>, IdsMapper<T>, Marker {
}