package com.iakuil.bf.common.tool;

import lombok.experimental.UtilityClass;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 对象属性复制工具
 * <p>性能略低于Mapstruct，适用于懒得创建{@link com.iakuil.bf.common.BaseConverter}方法的场景。</p><br/>
 * <p>注意：属性名称相同而类型不同的属性不会被拷贝。</p>
 */
@UtilityClass
public class BeanUtils {

    private static MultiKeyMap cache = new MultiKeyMap();

    /**
     * 对象属性复制
     */
    public static <T> T copy(Object from, Class<T> to) {
        if (from == null) {
            return null;
        }

        T toObj = getInstance(to);
        getCopier(from.getClass(), to).copy(from, toObj, null);
        return toObj;
    }

    /**
     * 批量对象属性复制
     */
    public static <T> List<T> copyMany(Collection<?> from, Class<T> to) {
        if (from == null || from.size() == 0) {
            return null;
        }

        List<T> results = new ArrayList<>();
        BeanCopier copier = getCopier(from.stream().findFirst().get().getClass(), to);
        for (Object obj : from) {
            T toObj = getInstance(to);
            copier.copy(obj, toObj, null);
            results.add(toObj);
        }

        return results;
    }

    private <T> T getInstance(Class<T> clazz) {
        T inst;
        try {
            inst = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("InstantiationException", e);
        }

        return inst;
    }

    private static BeanCopier getCopier(Class<?> from, Class<?> to) {
        BeanCopier copier;
        Object cached = cache.get(from, to);
        if (cached == null) {
            copier = BeanCopier.create(from, to, false);
            cache.put(from, to, copier);
        } else {
            copier = ((BeanCopier) cached);
        }

        return copier;
    }
}