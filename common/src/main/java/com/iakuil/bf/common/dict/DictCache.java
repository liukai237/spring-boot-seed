package com.iakuil.bf.common.dict;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 简单的数据字典缓存
 *
 * <p>系统支持两种数据字典：枚举类型和数据库表类型。
 * <p>前者在缓存初始化时放入缓存；
 * <p>后者通过定时任务定时刷新并放入缓存。
 * <p>BTW. 分布式场景应该封装成单独的服务。
 *
 * @author Kai
 */
@Slf4j
public class DictCache {

    private static DictCache instance;

    /**
     * 按类型缓存的数据字典
     */
    public static Map<String, List<DictItem>> cache = new ConcurrentHashMap<>(128);

    private DictCache() {
        pushAll(collectFromEnums());
    }

    public static DictCache getInstance() {
        if (instance == null) {
            instance = new DictCache();
        }
        return instance;
    }

    /**
     * 批量添加字典内容到缓存
     */
    public void pushAll(List<DictItem> items) {
        Map<String, List<DictItem>> dicts = items.stream().collect(Collectors.groupingBy(DictItem::getType));
        cache.putAll(dicts);
    }

    /**
     * 通过字典类型获取字典编码
     */
    public List<DictItem> getByType(String type) {
        return cache.get(type);
    }

    /**
     * 通过字典编码获取字典翻译名称
     *
     * <p>注意是name，不是value
     */
    public String getName(String type, String value) {
        if (type != null && cache.containsKey(type)) {
            Optional<DictItem> item = cache.get(type).stream().filter(dictItem -> dictItem.getValue().equals(value)).findFirst();
            if (item.isPresent()) {
                return item.get().getName();
            }
        }
        return null;
    }

    /**
     * 创建实例时加载所有字典枚举
     */
    private List<DictItem> collectFromEnums() {
        List<DictItem> dictItems = new ArrayList<>();
        Reflections reflections = new Reflections(getClass().getPackage().getName());
        Set<Class<? extends DictEnum>> enumsClass = reflections.getSubTypesOf(DictEnum.class);
        for (Class<? extends DictEnum> aClass : enumsClass) {
            if (!Enum.class.isAssignableFrom(aClass)) {
                log.warn("only support Enum class, unexpected class : {}", aClass.getName());
                continue;
            }

            DictEnum[] constants = aClass.getEnumConstants();
            for (DictEnum constant : constants) {
                String description = constant.description();
                dictItems.add(new DictItem(aClass.getSimpleName(),
                        StringUtils.isBlank(description) ? aClass.getSimpleName() : description,
                        constant.getValue().toString(),
                        constant.getName(), ((Enum) constant).ordinal()));
            }
        }

        return dictItems;
    }

    /**
     * 字典项
     *
     * <p>用于统一所有的数据字典格式。
     *
     * @author Kai
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DictItem implements Serializable {
        /**
         * 字典类型编码
         */
        private String type;

        /**
         * 字典类型描述
         */
        private String description;

        /**
         * 字段的value值
         */
        private String value;

        /**
         * 字段的翻译值
         */
        private String name;

        /**
         * 排序
         */
        private Integer sort;
    }
}
