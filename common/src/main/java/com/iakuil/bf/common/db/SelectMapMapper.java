package com.iakuil.bf.common.db;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;
import java.util.Map;

/**
 * 通过Map进行查询
 *
 * @author Kai
 */
@RegisterMapper
public interface SelectMapMapper<T> {

    /**
     * Map作为过滤参数进行查询
     *
     * <p>如果存在pageSize/pageNum的key则自动分页排序
     */
    @SelectProvider(
            type = SelectMapProvider.class,
            method = "dynamicSQL"
    )
    List<T> selectMap(Map<String, Object> params);
}