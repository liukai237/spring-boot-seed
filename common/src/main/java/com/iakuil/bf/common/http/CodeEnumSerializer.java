package com.iakuil.bf.common.http;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.iakuil.bf.common.CodeEnum;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * 全局枚举转JSON
 * <p>返回枚举name（小写），不暴露数据库真实code。</p>
 */
@JsonComponent
public class CodeEnumSerializer extends JsonSerializer<CodeEnum> {
    @Override
    public void serialize(CodeEnum anEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(anEnum.toString().toLowerCase());
    }
}