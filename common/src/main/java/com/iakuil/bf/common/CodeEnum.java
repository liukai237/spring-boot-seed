package com.iakuil.bf.common;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public interface CodeEnum<T extends Serializable> {
    String getName();

    @JsonValue
    T getValue();
}