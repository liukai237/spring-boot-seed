package com.iakuil.bf.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 数据字典池
 * <p>包括枚举类型和数据字典表类型<p/>
 *
 * @author Kai
 */
@Slf4j
public class DictPool {

    private static DictPool instance;

    public static Map<String, List<DictItem>> cache = new ConcurrentHashMap<>(128);

    private DictPool() {
        pushDictItems(collectDictEnums());
    }

    public static DictPool getInstance() {
        if (instance == null) {
            instance = new DictPool();
        }
        return instance;
    }

    /**
     * 批量添加字典内容
     */
    public void pushDictItems(List<DictItem> items) {
        Map<String, List<DictItem>> dicts = items.stream().collect(Collectors.groupingBy(DictItem::getType));
        cache.putAll(dicts);
    }

    /**
     * 通过字典类型获取字典编码
     */
    public List<DictItem> getDict(String code) {
        return cache.get(code);
    }

    /**
     * 通过字典编码,字典值取字典翻译名称
     */
    public String getDictName(String code, String value) {
        if (code != null && cache.containsKey(code)) {
            Optional<DictItem> findDict = cache.get(code).stream().filter(dictItem -> dictItem.getValue().equals(value)).findFirst();
            if (findDict.isPresent()) {
                return findDict.get().getName();
            }
        }
        return null;
    }

    /**
     * 创建Pool时加载所有字典枚举
     */
    private List<DictItem> collectDictEnums() {
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
}
