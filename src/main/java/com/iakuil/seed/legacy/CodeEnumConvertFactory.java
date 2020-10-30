package com.iakuil.seed.legacy;

import com.iakuil.seed.common.CodeEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * JSON/枚举转换器工厂
 *
 * <p>如果Java Bean中枚举属性实现了{@link CodeEnum}接口，则可通过value字段映射。</p>
 */
@Deprecated
public class CodeEnumConvertFactory implements ConverterFactory<String, CodeEnum> {

    @Override
    public <T extends CodeEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnum<>(targetType);
    }

    @SuppressWarnings("all")
    private class StringToEnum<T extends CodeEnum> implements Converter<String, T> {

        private final T[] values;

        public StringToEnum(Class<T> targetType) {
            values = targetType.getEnumConstants();
        }

        @Override
        public T convert(String source) {
            for (T t : values) {
                if (t.toString().toLowerCase().equals(source)) {
                    return t;
                }
            }
            return null;
        }
    }
}
