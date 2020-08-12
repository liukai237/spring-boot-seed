package com.iakuil.seed.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * 全局JSON转枚举
 */
@JsonComponent
public class LabelEnumSerializer extends JsonSerializer<CodeEnum> {
    @Override
    public void serialize(CodeEnum codeEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(codeEnum.getLabel());
    }
}