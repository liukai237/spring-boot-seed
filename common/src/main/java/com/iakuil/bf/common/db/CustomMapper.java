package com.iakuil.bf.common.db;

import com.iakuil.bf.common.PageQuery;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;
import java.util.Map;

/**
 * 自定义的数据库操作方法
 *
 * @author Kai
 */
@RegisterMapper
public interface CustomMapper<T> {

    /**
     * Map作为过滤参数进行查询
     * 如果存在pageSize/pageNum的key则自动分页排序
     */
    @SelectProvider(
            type = CustomSelectProvider.class,
            method = "dynamicSQL"
    )
    List<T> selectMap(Map<String, Object> params);

    /**
     * 使用封装好的对象作为过滤参数进行查询
     */
    @SelectProvider(
            type = CustomSelectProvider.class,
            method = "dynamicSQL"
    )
    List<T> selectPage(PageQuery condition);
}