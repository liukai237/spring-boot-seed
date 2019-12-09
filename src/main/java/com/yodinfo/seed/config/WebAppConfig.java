package com.yodinfo.seed.config;

import com.yodinfo.seed.util.CodeEnumConvertFactory;
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
     * 字符串转枚举支持
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new CodeEnumConvertFactory());
    }
}