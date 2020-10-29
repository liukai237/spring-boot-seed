package com.iakuil.seed.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * 全局枚举转JSON
 */
@JsonComponent
public class LabelEnumDeserializer extends JsonDeserializer<LabelEnum> {

    @Override
    public LabelEnum deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String currentName = jp.currentName();
        Object currentValue = jp.getCurrentValue();
        Class<?> type = BeanUtils.findPropertyType(currentName, currentValue.getClass());
        return getEnumByValue(type, node.textValue());
    }

    LabelEnum getEnumByValue(Class<?> clazz, String value) {
        for (Object enumObj : clazz.getEnumConstants()) {
            LabelEnum anEnum = (LabelEnum) enumObj;
            if (anEnum.getLabel().equals(value)) {
                return anEnum;
            }
        }

        return null;
    }
}