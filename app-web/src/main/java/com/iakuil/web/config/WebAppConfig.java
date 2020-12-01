package com.iakuil.web.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.iakuil.common.http.DateParamResolver;
import com.iakuil.common.http.ResolverBeanPostProcessor;
import com.iakuil.common.http.SortingParamResolver;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

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