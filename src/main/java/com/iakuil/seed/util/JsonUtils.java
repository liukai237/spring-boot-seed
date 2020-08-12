package com.iakuil.seed.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.iakuil.seed.exception.BusinessException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String bean2Json(Object obj) {
        String result;

        try {
            result = objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Occurring an exception during object parsing!", e);
        }

        return result;
    }

    public static <T> T json2bean(String jsonStr, Class<T> clazz) {
        T result;

        try {
            result = objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(jsonStr, clazz);
        } catch (IOException e) {
            throw new BusinessException("Occurring an exception during json parsing!", e);
        }

        return result;
    }

    public static <T> List<T> json2List(String jsonStr, Class<T> clazz) {
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);

        List<T> result;
        try {
            result = objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(jsonStr, listType);
        } catch (IOException e) {
            throw new BusinessException("Occurring an exception during json parsing!", e);
        }

        return result;
    }

    public static <T> T transfer(Object obj, Class<T> clazz) {
        return objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .convertValue(obj, clazz);
    }
}