package com.yodinfo.seed.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yodinfo.seed.bo.Req;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.beans.*;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

@ControllerAdvice
public class JsonBodyValidatorAdvice implements RequestBodyAdvice {
    private ThreadLocal<ObjectMapper> mapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    private static final Class[] annos = {
            RequestMapping.class,
            GetMapping.class,
            PostMapping.class,
            DeleteMapping.class,
            PutMapping.class
    };

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        AnnotatedElement element = methodParameter.getAnnotatedElement();
        return Arrays.stream(annos).anyMatch(anno -> anno.isAnnotation() && element.isAnnotationPresent(anno));
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        return httpInputMessage;
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        if (o instanceof Req) {
            Object params = ((Req) o).getParam();
            handleParamsInBody(params);
        }

        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    private void handleParamsInBody(Object obj) {
        BeanInfo beanInfo;

        try {
            beanInfo = Introspector.getBeanInfo(obj.getClass());
        } catch (IntrospectionException e) {
            throw new IllegalStateException(e); // it can't happen here
        }

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();

            // 过滤class属性
            if (key.equals("class")) {
                continue;
            }

            // 得到property对应的getter方法
            Method getter = property.getReadMethod();
            if (getter.getAnnotation(Transient.class) != null) {
                continue; // 带Transient注解的不校验
            }
            Object tmpValue;
            try {
                tmpValue = getter.invoke(obj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }

            Method setter = property.getWriteMethod();
            try {
                // 处理空字符串
                if (tmpValue instanceof String) {
                    String v = (String) tmpValue;
                    if (StringUtils.isBlank(v)) {
                        setter.invoke(obj, (Object) null);
                    }
                }

                // 过滤数组中空白字符串成员
                if (tmpValue instanceof String[]) {
                    String[] v = (String[]) tmpValue;
                    String[] arr = Arrays.stream(v).filter(StringUtils::isNoneBlank).toArray(String[]::new);
                    setter.invoke(obj, (Object) arr);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
