package com.yodinfo.seed.common;

import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.IdsMapper;

/**
 * CRUD Mapper基类
 *
 * <p>参考JPA实现基本的CRUD和批量插入方法。</p><br/>
 * <p>复杂的查询推荐使用MyBatis原生的XML，以实现SQL与业务代码分离。</p><br/>
 * <p>PS. MBG生成的代码不建议使用{@code Example}等对象查询。</p>
 *
 * @param <T> Entity
 */
public interface CrudMapper<T> extends BaseMapper<T>, IdsMapper<T>, InsertListMapper<T> {
}