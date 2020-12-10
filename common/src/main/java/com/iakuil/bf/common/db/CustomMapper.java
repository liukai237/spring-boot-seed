package com.iakuil.bf.common.db;

import com.iakuil.bf.common.PageQuery;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;
import java.util.Map;

/**
 * 自定义的数据库操作方法
 */
@RegisterMapper
public interface CustomMapper<T> {
    @SelectProvider(
            type = CustomSelectProvider.class,
            method = "dynamicSQL"
    )
    List<T> selectMap(Map<String, Object> params);

    @SelectProvider(
            type = CustomSelectProvider.class,
            method = "dynamicSQL"
    )
    List<T> selectPage(PageQuery condition);
}
