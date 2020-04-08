package com.yodinfo.seed.common;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Domain基类
 * <p>继承后自动实现{@link Serializable}接口和{@code toString()}方法。</p>
 */
public class BaseDomain implements Serializable {

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}