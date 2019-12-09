package com.yodinfo.seed.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class CodeEnumSerializer extends JsonSerializer<BaseCodeEnum> {
    @Override
    public void serialize(BaseCodeEnum baseCodeEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(baseCodeEnum.getValue());
    }
}