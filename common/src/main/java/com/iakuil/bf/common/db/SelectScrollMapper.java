package com.iakuil.bf.common.db;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * 滚动查询（试验功能）
 *
 * @author Kai
 */
@RegisterMapper
public interface SelectScrollMapper<T> {
    /**
     * 根据游标及条件查询实体
     *
     * @param record   查询实体条件
     * @param scrollId 游标ID
     */
    @SelectProvider(
            type = SelectScrollProvider.class,
            method = "dynamicSQL"
    )
    List<T> selectByScrollId(@Param("record") T record, @Param("scrollId") Long scrollId);
}
