package com.iakuil.bf.common.http;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义的URL参数处理器
 *
 * @author Kai
 */
public class ResolverBeanPostProcessor implements BeanPostProcessor {

    private HandlerMethodArgumentResolver[] specificResolvers;

    public ResolverBeanPostProcessor(HandlerMethodArgumentResolver... specificResolvers) {
        this.specificResolvers = specificResolvers;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if ("requestMappingHandlerAdapter".equals(beanName)) {
            RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) bean;
            List<HandlerMethodArgumentResolver> argumentResolvers = adapter.getArgumentResolvers();
            argumentResolvers = addArgumentResolvers(argumentResolvers);
            adapter.setArgumentResolvers(argumentResolvers);
        }
        return bean;
    }

    private List<HandlerMethodArgumentResolver> addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

        // 将自定义的ArgumentResolver添加到最前面
        if (specificResolvers != null) {
            resolvers.addAll(Arrays.stream(specificResolvers).collect(Collectors.toList()));
        }

        resolvers.addAll(argumentResolvers);
        return resolvers;
    }
}
