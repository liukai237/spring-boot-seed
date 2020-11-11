package com.iakuil.seed.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iakuil.seed.common.http.StartDateDeserializer;

import java.lang.annotation.*;

/**
 * 开始时间时间
 * <p>用于URL参数或者JSON Body，精确到00:00:00:000</p>
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonDeserialize(using = StartDateDeserializer.class)
public @interface StartDate {
}