package com.yodinfo.seed.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.yodinfo.seed.support.Strings;

import java.io.IOException;

public class SortDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String sort = jsonParser.getText();
        return Strings.parseOrderBy(sort);
    }
}
