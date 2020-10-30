package com.iakuil.seed.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * 全局枚举转JSON
 * <p>返回枚举name（小写）与描述信息，不暴露数据库真实code。</p>
 */
@JsonComponent
public class CodeEnumSerializer extends JsonSerializer<CodeEnum> {
    @Override
    public void serialize(CodeEnum anEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("code", anEnum.toString().toLowerCase());
        jsonGenerator.writeStringField("desc", anEnum.getDesc());
        jsonGenerator.writeEndObject();
    }
}