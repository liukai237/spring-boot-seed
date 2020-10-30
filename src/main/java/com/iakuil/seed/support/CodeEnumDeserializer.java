package com.iakuil.seed.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * 全局枚举转JSON
 */
@Deprecated
@JsonComponent
public class CodeEnumDeserializer extends JsonDeserializer<CodeEnum> {

    @Override
    public CodeEnum deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String currentName = jp.currentName();
        Object currentValue = jp.getCurrentValue();
        Class<?> type = BeanUtils.findPropertyType(currentName, currentValue.getClass());
        return getEnumByValue(type, node.numberValue());
    }

    CodeEnum getEnumByValue(Class<?> clazz, Number value) {
        for (Object enumObj : clazz.getEnumConstants()) {
            CodeEnum anEnum = (CodeEnum) enumObj;
            if (anEnum.getCode().equals(value.intValue())) {
                return anEnum;
            }
        }

        return null;
    }
}