/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 342252328@qq.com
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
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
    private static Field getField(Object obj, String fieldName) {
        Field field = null;
        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                // do nothing
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
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 调用对象方法返回
     *
     * @param obj        需要调用的对象
     * @param methodNmae 方法名称
     * @return 调用方法返回的值
     */
    public static Object transferMethod(Object obj, String methodNmae) {
        try {
            Class<? extends Object> cls = obj.getClass();
            // 获得类的私有方法
            Method method = cls.getDeclaredMethod(methodNmae, null);
            method.setAccessible(true);
            // 调用该方法
            return method.invoke(obj, null);
        } catch (Exception e) {
            return new RuntimeException(e);
        }
    }
}
