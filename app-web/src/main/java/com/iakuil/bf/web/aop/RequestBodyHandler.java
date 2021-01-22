package com.iakuil.bf.web.aop;

import com.iakuil.bf.common.DictPool;
import com.iakuil.bf.common.PageQuery;
import com.iakuil.bf.common.annotation.DictType;
import com.iakuil.bf.common.constant.RespCode;
import com.iakuil.bf.common.exception.BusinessException;
import com.iakuil.bf.common.tool.Strings;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局JSON参数处理
 */
@ControllerAdvice
public class RequestBodyHandler implements RequestBodyAdvice {

    private static final Class[] ANNOS = {
            RequestMapping.class,
            GetMapping.class,
            PostMapping.class,
            DeleteMapping.class,
            PutMapping.class
    };

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        AnnotatedElement element = methodParameter.getAnnotatedElement();
        return Arrays.stream(ANNOS).anyMatch(anno -> anno.isAnnotation() && element.isAnnotationPresent(anno));
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        return httpInputMessage;
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        if (o instanceof PageQuery) {
            Object params = ((PageQuery) o).getFilter();
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
            String fieldName = property.getName();

            // 过滤class属性
            if ("class".equals(fieldName)) {
                continue;
            }

            // 得到property对应的getter方法
            Method getter = property.getReadMethod();
            if (getter == null || getter.getAnnotation(Transient.class) != null) {
                continue; // 带Transient注解的不校验
            }
            Object fieldValue;
            try {
                fieldValue = getter.invoke(obj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }

            if (fieldValue == null) {
                continue;
            }

            // 校验非枚举类型数据字典
            Field field;
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                throw new IllegalStateException(e); // it can't happen here
            }
            DictType dictType = AnnotationUtils.findAnnotation(field, DictType.class);
            if (dictType != null) {
                String type = StringUtils.isBlank(dictType.value()) ? Strings.toUnderlineCase(fieldName) : dictType.value();
                checkDictValue(type, fieldValue);
            }

            // 处理空字符串和数组中空白字符串成员
            Method setter = property.getWriteMethod();
            try {
                if (fieldValue instanceof String) {
                    String v = (String) fieldValue;
                    if (StringUtils.isBlank(v)) {
                        setter.invoke(obj, (Object) null);
                    }
                }

                if (fieldValue instanceof String[]) {
                    String[] v = (String[]) fieldValue;
                    String[] arr = Arrays.stream(v).filter(StringUtils::isNoneBlank).toArray(String[]::new);
                    setter.invoke(obj, (Object) arr);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private void checkDictValue(String dictType, Object dictValue) {
        List<DictPool.DictItem> dictItems = DictPool.getInstance().getDict(dictType);
        if (dictItems == null) {
            throw new BusinessException("Invalid dict type: " + dictType, RespCode.BAD_REQUEST.getCode());
        }

        String typeDesc = dictItems.get(0).getDescription();
        String errorTemplate = "Invalid dict value: [%s] for 【" + typeDesc + "】, allowed values: " + dictItems.stream()
                .sorted(Comparator.comparing(DictPool.DictItem::getSort))
                .map(item -> item.getValue() + ":" + item.getName())
                .collect(Collectors.joining(","));
        List<String> allowValues = dictItems.stream().sorted(Comparator.comparing(DictPool.DictItem::getSort)).map(DictPool.DictItem::getValue).collect(Collectors.toList());
        if (dictValue instanceof String && !allowValues.contains(dictValue.toString())) {
            throw new BusinessException(String.format(errorTemplate, dictValue.toString()));
        }
        if (dictValue instanceof String[]) {
            for (String item : (String[]) dictValue) {
                if (!allowValues.contains(item)) {
                    throw new BusinessException(String.format(errorTemplate, item));
                }
            }
        }
        if (dictValue instanceof Collection) {
            for (String item : (Collection<String>) dictValue) {
                if (!allowValues.contains(item)) {
                    throw new BusinessException(String.format(errorTemplate, item));
                }
            }
        }
    }
}