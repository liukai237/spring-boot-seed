package com.yodinfo.seed.config;

import com.yodinfo.seed.support.CodeEnumConvertFactory;
import com.yodinfo.seed.support.DateParamResolver;
import com.yodinfo.seed.support.ResolverBeanPostProcessor;
import com.yodinfo.seed.support.SortingParamResolver;
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
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS")
                .maxAge(3600 * 24);
    }

    /**
     * Json Body处理
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new CodeEnumConvertFactory()); // 字符串转枚举支持
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
}