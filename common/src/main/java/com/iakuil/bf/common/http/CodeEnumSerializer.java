package com.iakuil.bf.common.http;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.iakuil.bf.common.CodeEnum;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * 全局枚举转JSON
 */
@JsonComponent
public class CodeEnumSerializer extends JsonSerializer<CodeEnum> {
    @Override
    public void serialize(CodeEnum value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value == null) {
            jsonGenerator.writeNull();
        } else {
            jsonGenerator.writeObject(value.getValue());
            jsonGenerator.writeFieldName(jsonGenerator.getOutputContext().getCurrentName() + "Name");
            jsonGenerator.writeString(value.getName());
        }
    }

    @Override
    public Class handledType() {
        return CodeEnum.class;
    }
}