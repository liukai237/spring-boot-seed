package com.iakuil.bf.common;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * 数据字典的枚举实现
 *
 * <p>枚举类继承该接口后，自动实现JSON序列化与反序列化，以及数据库字典字段映射。
 *
 * @author Kai
 */
public interface DictEnum<T extends Serializable> {

    /**
     * 字典类型名称
     */
    String getName();

    /**
     * 字典类型值
     */
    @JsonValue
    T getValue();

    /**
     * 字典类型描述
     */
    String description();
}