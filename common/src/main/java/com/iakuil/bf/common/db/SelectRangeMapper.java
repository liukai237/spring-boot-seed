package com.iakuil.bf.common.db;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * 范围查询
 *
 * <p>试验功能，不建议大规模使用。
 *
 * @author Kai
 */
@RegisterMapper
public interface SelectRangeMapper<T> {

    /**
     * 实验功能
     *
     * <p>pagehelper.supportMethodsArguments=true的情况下，
     * <p>如果keySet中存在pageSize/pageNum/orderBay，则自动实现分页排序
     */
    @SelectProvider(
            type = SelectRangeProvider.class,
            method = "dynamicSQL"
    )
    List<T> selectInRange(Object params);
}