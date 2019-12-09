package com.yodinfo.seed.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class CodeEnumConvertFactory implements ConverterFactory<String, BaseCodeEnum> {

    @Override
    public <T extends BaseCodeEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnum<>(targetType);
    }

    @SuppressWarnings("all")
    private class StringToEnum<T extends BaseCodeEnum> implements Converter<String, T> {

        private final T[] values;

        public StringToEnum(Class<T> targetType) {
            values = targetType.getEnumConstants();
        }

        @Override
        public T convert(String source) {
            for (T t : values) {
                if (t.getValue().equals(source)) {
                    return t;
                }
            }
            return null;
        }
    }
}
