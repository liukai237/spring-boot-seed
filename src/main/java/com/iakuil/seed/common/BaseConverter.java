package com.iakuil.seed.common;

import java.util.List;

/**
 * 实体映射工具类基类
 * <p>目前为止性能最佳的转换工具（仅次于getter/setter）。</p><br/>
 * <p>建议所有Entity和DTO均通过此工具进行转换，以实现业务与转换逻辑分离。
 * 简单且不可复用的场景可以考虑{@link com.iakuil.seed.common.tool.BeanUtils}。</p>
 *
 * @param <T> Entity
 * @param <K> DTO
 */
public interface BaseConverter<T, K> {
    K toDto(T entity);

    List<K> toDtoList(List<T> entityList);

    T toEntity(K dto);

    List<T> toEntityList(List<K> dtoList);
}