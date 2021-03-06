package com.iakuil.bf.common.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iakuil.bf.common.http.EndDateDeserializer;

import java.lang.annotation.*;

/**
 * 结束时间
 *
 * <p>用于URL参数或者JSON Body，精确到23:59:59:999
 *
 * @author Kai
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonDeserialize(using = EndDateDeserializer.class)
public @interface EndDate {
}