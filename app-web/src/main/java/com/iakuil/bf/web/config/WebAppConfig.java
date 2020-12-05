package com.iakuil.bf.web.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.iakuil.bf.common.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Autowired
    private CodeEnumConvertFactory codeEnumConvertFactory;

    @Autowired(required = false)
    DictAnnotationIntrospector dictAnnotationIntrospector;

    /**
     * 跨域支持
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600 * 24);
    }

    /**
     * 带注解的数据字典转换
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.stream().filter(converter -> converter instanceof MappingJackson2HttpMessageConverter).forEach(converter -> {
            MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
            jsonConverter.getObjectMapper().setAnnotationIntrospector(dictAnnotationIntrospector);
        });
    }

    /**
     * 枚举数据字典转换
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(codeEnumConvertFactory);
    }

    /**
     * JSON反序列化时，全局Long转字符串
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer builderCustomizer() {
        return builder -> {
            builder.serializerByType(Long.class, ToStringSerializer.instance);
        };
    }

    /**
     * URL排序及日期参数处理
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
}