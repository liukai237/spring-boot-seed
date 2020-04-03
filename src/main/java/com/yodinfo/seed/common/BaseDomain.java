package com.yodinfo.seed.common;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class BaseDomain implements Serializable {

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}