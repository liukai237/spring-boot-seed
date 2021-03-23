package com.iakuil.bf.common.db;

import org.apache.ibatis.annotations.DeleteProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 通过ID列表进行删除（自定义通用方法）
 *
 * <p>解决tkMapper原生deleteByIds不支持逻辑删除的问题。
 *
 * @author Kai
 */
@RegisterMapper
public interface DeleteByIdsMapper<T> {

    @DeleteProvider(
            type = DeleteByIdsProvider.class,
            method = "dynamicSQL"
    )
    int deleteByIds(String var1);
}