package com.yodinfo.seed.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.yodinfo.seed.support.LabelEnumConvertFactory;
import com.yodinfo.seed.support.DateParamResolver;
import com.yodinfo.seed.support.ResolverBeanPostProcessor;
import com.yodinfo.seed.support.SortingParamResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    /**
     * 默认日期时间格式
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认日期格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    /**
     * 跨域支持
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .maxAge(3600 * 24);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // LocalDateTime序列化和反序列化配置
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));

        objectMapper.registerModule(javaTimeModule).registerModule(new ParameterNamesModule());
        return objectMapper;
    }

    /**
     * Json Body处理
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new LabelEnumConvertFactory()); // 字符串转枚举支持
        registry.addConverter(new LocalDateTime2StringConverter());
        registry.addConverter(new String2LocalDateTimeConverter());
        registry.addConverter(new LocalTime2StringConverter());
        registry.addConverter(new String2LocalTimeConverter());
        registry.addConverter(new LocalDate2StringConverter());
        registry.addConverter(new String2LocalDateConverter());
    }

    /**
     * Request Query处理
     */
    @Bean
    public ResolverBeanPostProcessor resolverBeanPostProcessor() {
        return new ResolverBeanPostProcessor(sortingParamResolver(), dateParamResolver());
    }

    @Bean
    public SortingParamResolver sortingParamResolver() {
        return new SortingParamResolver();
    }

    @Bean
    public DateParamResolver dateParamResolver() {
        return new DateParamResolver();
    }

    @Bean
    public Converter<String, LocalDateTime> string2LocalDateTimeConverter() {
        return new String2LocalDateTimeConverter();
    }

    @Bean
    public Converter<String, LocalTime> string2LocalTimeConverter() {
        return new String2LocalTimeConverter();
    }

    @Bean
    public Converter<String, LocalDate> string2LocalDateConverter() {
        return new String2LocalDateConverter();
    }

    public static class LocalDateTime2StringConverter implements Converter<LocalDateTime, String> {
        @Override
        public String convert(LocalDateTime source) {
            return source.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
        }
    }

    public static class String2LocalDateTimeConverter implements Converter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(String source) {
            return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
        }
    }

    public static class LocalTime2StringConverter implements Converter<LocalTime, String> {
        @Override
        public String convert(LocalTime source) {
            return source.format(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
        }
    }

    public static class String2LocalTimeConverter implements Converter<String, LocalTime> {
        @Override
        public LocalTime convert(String source) {
            return LocalTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
        }
    }

    public static class LocalDate2StringConverter implements Converter<LocalDate, String> {
        @Override
        public String convert(LocalDate source) {
            return source.format(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
        }
    }

    public static class String2LocalDateConverter implements Converter<String, LocalDate> {
        @Override
        public LocalDate convert(String source) {
            return LocalDate.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
        }
    }
}