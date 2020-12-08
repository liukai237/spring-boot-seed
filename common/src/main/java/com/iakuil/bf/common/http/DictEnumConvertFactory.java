package com.iakuil.bf.common.http;

import com.iakuil.bf.common.DictEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

/**
 * 枚举/数据字典转换工厂
 */
@Component
public class DictEnumConvertFactory implements ConverterFactory<String, DictEnum> {
    @Override
    public <T extends DictEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnum<>(targetType);
    }

    @SuppressWarnings("all")
    private static class StringToEnum<T extends DictEnum> implements Converter<String, T> {
        private Class<T> targerType;

        public StringToEnum(Class<T> targerType) {
            this.targerType = targerType;
        }

        @Override
        public T convert(String source) {
            if (StringUtils.isBlank(source)) {
                return null;
            }
            return (T) DictEnumConvertFactory.getEnum(this.targerType, source);
        }
    }

    public static <T extends DictEnum> Object getEnum(Class<T> targerType, String source) {
        for (T enumObj : targerType.getEnumConstants()) {
            if (source.equals(String.valueOf(enumObj.getName()))) {
                return enumObj;
            }
        }
        return null;
    }
}