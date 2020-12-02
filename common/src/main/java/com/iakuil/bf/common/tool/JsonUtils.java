package com.iakuil.bf.common.tool;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String bean2Json(Object obj) {
        String result;

        try {
            result = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Occurring an exception during object parsing!", e);
        }

        return result;
    }

    public static <T> T json2bean(String jsonStr, Class<T> clazz) {
        T result;

        try {
            result = OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (IOException e) {
            throw new IllegalStateException("Occurring an exception during json parsing!", e);
        }

        return result;
    }

    public static <T> List<T> json2List(String jsonStr, Class<T> clazz) {
        CollectionType listType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, clazz);

        List<T> result;
        try {
            result = OBJECT_MAPPER.readValue(jsonStr, listType);
        } catch (IOException e) {
            throw new IllegalStateException("Occurring an exception during json parsing!", e);
        }

        return result;
    }

    public static <T> T transfer(Object obj, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(obj, clazz);
    }
}