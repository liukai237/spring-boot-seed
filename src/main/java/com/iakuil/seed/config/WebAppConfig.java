package com.iakuil.seed.config;

import com.iakuil.seed.support.DateParamResolver;
import com.iakuil.seed.support.LabelEnumConvertFactory;
import com.iakuil.seed.support.ResolverBeanPostProcessor;
import com.iakuil.seed.support.SortingParamResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
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
     * Request Query处理
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new LabelEnumConvertFactory()); // 字符串转枚举支持
    }

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