package com.yodinfo.seed.common;

import java.util.List;

/**
 * 实体映射工具类基类
 * <p>可使代码更加优雅，不强制要求继承。</p>
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