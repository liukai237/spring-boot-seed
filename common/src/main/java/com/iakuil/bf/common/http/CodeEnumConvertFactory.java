package com.iakuil.bf.common.http;

import com.iakuil.bf.common.CodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component
public class CodeEnumConvertFactory implements ConverterFactory<String, CodeEnum> {
    @Override
    public <T extends CodeEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToIEum<>(targetType);
    }

    @SuppressWarnings("all")
    private static class StringToIEum<T extends CodeEnum> implements Converter<String, T> {
        private Class<T> targerType;

        public StringToIEum(Class<T> targerType) {
            this.targerType = targerType;
        }

        @Override
        public T convert(String source) {
            if (StringUtils.isBlank(source)) {
                return null;
            }
            return (T) CodeEnumConvertFactory.getIEnum(this.targerType, source);
        }
    }

    public static <T extends CodeEnum> Object getIEnum(Class<T> targerType, String source) {
        for (T enumObj : targerType.getEnumConstants()) {
            if (source.equals(String.valueOf(enumObj.getName()))) {
                return enumObj;
            }
        }
        return null;
    }
}