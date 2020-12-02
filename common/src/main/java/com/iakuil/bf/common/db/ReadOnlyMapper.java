package com.iakuil.bf.common.db;

import tk.mybatis.mapper.common.base.BaseSelectMapper;
import tk.mybatis.mapper.common.ids.SelectByIdsMapper;

/**
 * 只读Mapper基类
 *
 * <p>仅提供读取数据库方法。不建议使用代码生成器。</p><br/>
 *
 * @param <T> Entity
 */
public interface ReadOnlyMapper<T> extends BaseSelectMapper<T>, SelectByIdsMapper<T> {
}