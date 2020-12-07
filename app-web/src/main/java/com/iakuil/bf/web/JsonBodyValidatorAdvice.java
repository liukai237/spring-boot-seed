package com.iakuil.bf.web;

import com.iakuil.bf.common.DictItem;
import com.iakuil.bf.common.DictPool;
import com.iakuil.bf.common.Req;
import com.iakuil.bf.common.annotation.DictType;
import com.iakuil.bf.common.constant.RespCode;
import com.iakuil.bf.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.beans.*;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局JSON参数处理
 */
@ControllerAdvice
public class JsonBodyValidatorAdvice implements RequestBodyAdvice {

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
            Object params = ((Req) o).getFilter();
            handleParamsInBody(params);
        } else {
            handleParamsInBody(o);
        }

        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    private void handleParamsInBody(Object obj) {
        if (obj == null) {
            return;
        }

        Class<?> clazz = obj.getClass();
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            throw new IllegalStateException(e); // it can't happen here
        }

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();

            // 过滤class属性
            if ("class" .equals(key)) {
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

            if (tmpValue == null) {
                continue;
            }

            // 校验非枚举类型数据字典
            Field field = null;
            try {
                field = clazz.getDeclaredField(key);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            DictType dictType = AnnotationUtils.findAnnotation(field, DictType.class);
            if (dictType != null) {
                String code = dictType.value();
                List<DictItem> dictItems = DictPool.getInstance().getDict(code);
                if (dictItems == null) {
                    throw new BusinessException("Invalid dict type: " + code, RespCode.BAD_REQUEST.getCode());
                }
                String typeDesc = dictItems.get(0).getDescription();
                List<String> allowValues = dictItems.stream().map(DictItem::getValue).collect(Collectors.toList());
                if (tmpValue instanceof String && !allowValues.contains(tmpValue.toString())) {
                    throw new BusinessException("Invalid dict value: " + tmpValue + ", allowed values: " + StringUtils.join(allowValues, ","), RespCode.BAD_REQUEST.getCode());
                }
                if (tmpValue instanceof String[] && !allowValues.containsAll(Arrays.asList((String[]) tmpValue))) {
                    throw new BusinessException("Invalid dict values for " + typeDesc + ": " + Arrays.asList((String[]) tmpValue) + ", allowed values: " + StringUtils.join(allowValues, ","), RespCode.BAD_REQUEST.getCode());
                }
                if (tmpValue instanceof Collection && !allowValues.containsAll((Collection) tmpValue)) {
                    throw new BusinessException("Invalid dict values for " + typeDesc + ": " + tmpValue + ", allowed values: " + StringUtils.join(allowValues, ","), RespCode.BAD_REQUEST.getCode());
                }
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