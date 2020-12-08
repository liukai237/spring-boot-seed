package com.iakuil.bf.common.tool;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import org.springframework.cglib.beans.BeanMap;

import java.util.List;
import java.util.Map;

/**
 * Java Bean与Map转换工具
 *
 * <p>封装了Spring框架自带{@link BeanMap}</p>
 */
@UtilityClass
public class BeanMapUtils {

    public static <T> Map<String, Object> beanToMap(T bean) {
        return beanToMap(bean, false);
    }

    public static <T> Map<String, Object> beanToMap(T bean, boolean ignoreNull) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                Object value = beanMap.get(key);
                if (value == null && ignoreNull) {
                    continue;
                }
                map.put(key + "", value);
            }
        }
        return map;
    }

    /**
     * 将map转换为javabean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        if (map != null) {
            BeanMap beanMap = BeanMap.create(bean);
            beanMap.putAll(map);
        }
        return bean;
    }

    /**
     * 将List<T>转换为List<Map<String, Object>>
     */
    public static <T> List<Map<String, Object>> objectsToMaps(List<T> objList) {
        List<Map<String, Object>> list = Lists.newArrayList();
        if (objList != null && objList.size() > 0) {
            Map<String, Object> map;
            T bean;
            for (T t : objList) {
                bean = t;
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 将List<Map<String,Object>>转换为List<T>
     */
    public static <T> List<T> mapsToObjects(List<Map<String, Object>> maps, Class<T> clazz) {
        List<T> list = Lists.newArrayList();
        if (maps != null && maps.size() > 0) {
            Map<String, Object> map;
            T bean;
            for (Map<String, Object> stringObjectMap : maps) {
                map = stringObjectMap;
                try {
                    bean = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new IllegalStateException("Occurring an exception during class instancing!", e);
                }
                mapToBean(map, bean);
                list.add(bean);
            }
        }
        return list;
    }
}