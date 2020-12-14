package com.iakuil.bf.common.annotation;

import com.iakuil.bf.common.db.JsonColumnTypeHandler;
import org.apache.ibatis.type.TypeHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将字段标注为JSON类型
 * <p>{@link tk.mybatis.mapper.annotation.ColumnType}的简化版本</p>
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonColumn {
    String column() default "";

    Class<?> type() default Object.class;

    Class<? extends TypeHandler<?>> typeHandler() default JsonColumnTypeHandler.class;
}
