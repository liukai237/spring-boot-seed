package com.iakuil.bf.common.http;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.iakuil.bf.common.dict.DictEnum;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * 全局枚举转JSON
 *
 * @author Kai
 */
@JsonComponent
public class DictEnumSerializer extends JsonSerializer<DictEnum> {
    @Override
    public void serialize(DictEnum value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
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
        return DictEnum.class;
    }
}