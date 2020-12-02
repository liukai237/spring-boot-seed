package com.iakuil.bf.common.http;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.iakuil.bf.common.annotation.StartDate;
import com.iakuil.bf.common.tool.Dates;

import java.io.IOException;
import java.util.Date;

@JacksonStdImpl
public class StartDateDeserializer extends JsonDeserializer<Date> implements ContextualDeserializer {

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String date = jsonParser.getText();
        return Dates.getDayStart(date);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        StartDate annotation = property.getAnnotation(StartDate.class);
        return annotation == null ? null : new StartDateDeserializer();
    }
}
