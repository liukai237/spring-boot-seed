package com.iakuil.bf.common;

import com.iakuil.toolkit.BeanUtils;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Entity/DTO映射工具类基类
 *
 * <p>目前为止性能最佳的转换工具（仅次于getter/setter）。
 * <p>建议所有Entity和DTO均通过此工具进行转换，以实现业务与转换逻辑分离。
 * VO与DTO转换请使用{@link BeanUtils}，或者手写getter/setter。
 *
 * @param <T> Entity
 * @param <K> DTO
 *
 * @author Kai
 */
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BaseConverter<T, K> {
    K toDto(T entity);

    List<K> toDtoList(List<T> entityList);

    T toEntity(K dto);

    List<T> toEntityList(List<K> dtoList);
}