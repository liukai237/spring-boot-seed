package com.iakuil.bf.common.db;

import org.apache.ibatis.annotations.InsertProvider;

/**
 * saveOrUpdate模式（试验功能）
 *
 * <p>判断主键是否存在，如果存在且不为空执行update语句，如果主键不存在或为空，执行insert语句。
 * <p>不处理乐观锁。
 */
public interface SaveOrUpdateMapper<T> {

    @InsertProvider(type = SaveOrUpdateProvider.class, method = "dynamicSQL")
    int saveOrUpdate(T record);
}
