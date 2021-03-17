package com.iakuil.bf.common.db;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * 游标查询（试验功能）
 *
 * @author Kai
 */
@RegisterMapper
public interface SelectCursorMapper<T> {
    /**
     * 根据游标及条件查询实体
     *
     * @param record 查询实体条件
     * @param nextId 游标ID
     */
    @SelectProvider(
            type = SelectCursorProvider.class,
            method = "dynamicSQL"
    )
    List<T> selectByNextId(@Param("record") T record, @Param("nextId") Long nextId);
}
