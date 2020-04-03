package com.yodinfo.seed.common;

import java.util.List;

public interface BaseConverter<T, K> {
    K toDto(T entity);

    List<K> toDtoList(List<T> entityList);

    T toEntity(K dto);

    List<T> toEntityList(List<K> dtoList);
}