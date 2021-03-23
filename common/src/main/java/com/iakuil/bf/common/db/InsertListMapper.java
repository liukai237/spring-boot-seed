package com.iakuil.bf.common.db;

import org.apache.ibatis.annotations.InsertProvider;

import java.util.List;

/**
 * MySQL批量插入（自定义通用方法）
 *
 * <p>解决tkMapper原生insertList不支持逻辑删除字段自动赋值的问题。
 */
public interface InsertListMapper<T> {

    @InsertProvider(type = InsertListProvider.class, method = "dynamicSQL")
    int insertList(List<? extends T> recordList);
}
