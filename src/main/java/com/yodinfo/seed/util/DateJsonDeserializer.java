package com.yodinfo.seed.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class DateJsonDeserializer extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String date = jsonParser.getText();
        if (StringUtils.isBlank(date)) {
            return null;
        }

        try {
            return DateUtils.parseDate(date, "yyyy-MM-dd", "HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "EEE MMM dd yyyy");
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
