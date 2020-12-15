package com.iakuil.bf.common.tool;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeBase;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 基于Jackson的JSON工具类
 */
@UtilityClass
public class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String bean2Json(Object obj) {
        if (obj == null) {
            return null;
        }

        String result;
        try {
            result = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Occurring an exception during object parsing!", e);
        }

        return result;
    }

    public static <T> T json2bean(String jsonStr, Class<T> clazz) {
        if (jsonStr == null || Strings.EMPTY.equals(jsonStr) || clazz == null) {
            return null;
        }

        T result;

        try {
            result = OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (IOException e) {
            throw new IllegalStateException("Occurring an exception during json parsing!", e);
        }

        return result;
    }

    public static <T> List<T> json2List(String jsonStr, Class<T> clazz) {
        return readType(jsonStr, OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, clazz));
    }

    public static <T> List<T> json2Set(String jsonStr, Class<T> clazz) {
        return readType(jsonStr, OBJECT_MAPPER.getTypeFactory().constructCollectionType(HashSet.class, clazz));
    }

    public static <T> List<T> json2Array(String jsonStr, Class<T> clazz) {
        return readType(jsonStr, OBJECT_MAPPER.getTypeFactory().constructArrayType(clazz));
    }

    private static <T> T readType(String jsonStr, TypeBase type) {
        if (jsonStr == null || Strings.EMPTY.equals(jsonStr) || type == null) {
            return null;
        }

        T result;
        try {
            result = OBJECT_MAPPER.readValue(jsonStr, type);
        } catch (IOException e) {
            throw new IllegalStateException("Occurring an exception during json parsing!", e);
        }

        return result;
    }
}