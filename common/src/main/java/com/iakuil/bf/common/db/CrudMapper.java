package com.iakuil.bf.common.db;

import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.ids.SelectByIdsMapper;

/**
 * CRUD Mapper基类
 *
 * <p>参考JPA实现基本的CRUD和批量插入方法。
 * <p>复杂的查询建议在MyBatis原生的XML Mapper中手写SQL，以实现SQL与业务代码分离。
 *
 * <p>PS. 如果没有范围查询，建议优先通用查询方法，而不是{@code Example}对象查询。
 *
 * @author Kai
 */
public interface CrudMapper<T> extends BaseMapper<T>,
        DeleteByIdsMapper<T>,
        SelectByIdsMapper<T>,
        InsertListMapper<T>,
        SelectCursorMapper<T>,
        ExampleMapper<T> {
}