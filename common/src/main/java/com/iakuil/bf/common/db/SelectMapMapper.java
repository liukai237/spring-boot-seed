package com.iakuil.bf.common.db;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;
import java.util.Map;

/**
 * 使用Map作为查询条件进行查询
 *
 * <p>主要用于遗留接口，不建议大规模使用。
 *
 * @author Kai
 */
@RegisterMapper
public interface SelectMapMapper<T> {

    /**
     * Map作为过滤参数进行查询
     *
     * <p>pagehelper.pageSizeZero=true的情况下，
     * <p>如果keySet中存在pageSize/pageNum/orderBay，则自动实现分页排序
     */
    @SelectProvider(
            type = SelectMapProvider.class,
            method = "dynamicSQL"
    )
    List<T> selectMap(Map<String, Object> params);
}