package com.iakuil.bf.common.tool;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * MyBatis插件工具类
 *
 * @author Kai
 */
public final class PluginUtils {

    private static final Log log = LogFactory.getLog(PluginUtils.class);

    private PluginUtils() {
    }

    /**
     * <p>
     * Recursive get the original target object.
     * <p>
     * If integrate more than a plugin, maybe there are conflict in these plugins,
     * because plugin will proxy the object.<br>
     * So, here get the original target object
     *
     * @param target proxy-object
     * @return original target object
     */
    public static Object processTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject mo = SystemMetaObject.forObject(target);
            return processTarget(mo.getValue("h.target"));
        }

        // must keep the result object is StatementHandler or ParameterHandler in
        // Optimistic Locker plugin
        if (!(target instanceof StatementHandler) && !(target instanceof ParameterHandler)) {
            if (log.isDebugEnabled()) {
                log.error("Optimistic Locker plugin init failed.");
            }
            throw new IllegalStateException("[Optimistic Locker Plugin Error] plugin init failed.");
        }
        return target;
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
        return getField(obj.getClass(), fieldName);
    }

    /**
     * 获取指定类里面的指定属性对象
     *
     * @param clz       目标类型
     * @param fieldName 指定属性名称
     * @return 属性对象
     */
    public static Field getField(Class<?> clz, String fieldName) {
        Field field = null;
        for (Class<?> clazz = clz; clazz != Object.class; clazz = clazz.getSuperclass()) {
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
