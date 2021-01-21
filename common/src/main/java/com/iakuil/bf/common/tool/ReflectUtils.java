package com.iakuil.bf.common.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Slf4j
public class ReflectUtils {
    public static boolean hasAnnotation(Object entity, Class annotation) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (AnnotationUtils.getAnnotation(field, annotation) != null) {
                return true;
            }
        }

        return false;
    }

    public static Object getValueByAnnotation(Object entity, Class annotation) {
        Object value = null;
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (AnnotationUtils.getAnnotation(field, annotation) != null) {
                field.setAccessible(true);
                try {
                    value = field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
                field.setAccessible(false);
            }
        }

        return value;
    }

    public static void setValueByAnnotation(Object entity, Object value, Class annotation) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (AnnotationUtils.getAnnotation(field, annotation) != null) {
                field.setAccessible(true);
                try {
                    field.set(entity, value);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
                field.setAccessible(false);
            }
        }
    }

    /**
     * 获取指定对象的指定属性
     *
     * @param obj       指定对象
     * @param fieldName 指定属性名称
     * @return 指定属性
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        Object result = null;
        Field field = getField(obj, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                result = field.get(obj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                log.error("[Occurring an exception during field value reading!]", e);
            }
        }
        return result;
    }

    /**
     * 获取指定对象里面的指定属性对象
     *
     * @param obj       目标对象
     * @param fieldName 指定属性名称
     * @return 属性对象
     */
    public static Field getField(Object obj, String fieldName) {
        Field field = null;
        for (Class<?> clazz = obj.getClass() == Class.class ? (Class<?>) obj : obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                log.error("There is no such Field: " + fieldName);
            }
        }
        return field;
    }

    /**
     * 设置指定对象的指定属性值
     *
     * @param obj        指定对象
     * @param fieldName  指定属性
     * @param fieldValue 指定属性值
     */
    public static void setFieldValue(Object obj, String fieldName, Object fieldValue) {
        Field field = getField(obj, fieldName);
        if (field != null) {
            try {
                field.setAccessible(true);
                field.set(obj, fieldValue);
                field.setAccessible(false);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                log.error("[Occurring an exception during field value setting!]", e);
            }
        }
    }

    /**
     * 调用对象方法返回
     *
     * @param obj        需要调用的对象
     * @param methodName 方法名称
     * @return 调用方法返回的值
     */
    public static Object transferMethod(Object obj, String methodName) {
        try {
            Class<?> cls = obj.getClass();
            // 获得类的私有方法
            Method method = cls.getDeclaredMethod(methodName, null);
            method.setAccessible(true);
            // 调用该方法
            return method.invoke(obj, null);
        } catch (Exception e) {
            return new IllegalStateException("[Occurring an exception during method invoking!]", e);
        }
    }
}
