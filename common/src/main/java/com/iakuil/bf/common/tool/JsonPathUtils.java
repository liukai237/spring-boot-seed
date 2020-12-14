package com.iakuil.bf.common.tool;

import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 使用Json Path的方式访问JSON
 * <p>读出的值尽量不要有嵌套。</p>
 */
public class JsonPathUtils {

    private static final TypeRef<Map<String, Object>> MAP_TYPE_REF = new TypeRef<Map<String, Object>>() {
    };

    private static final Configuration CONFIG;

    static {
        Configuration.setDefaults(new Configuration.Defaults() {
            private final JsonProvider jsonProvider = new JacksonJsonProvider();
            private final MappingProvider mappingProvider = new JacksonMappingProvider();

            @Override
            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            @Override
            public MappingProvider mappingProvider() {
                return mappingProvider;
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });

        CONFIG = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS);
    }

    public static String put(String json, String path, String key, Object value) {
        return parse(json, path).put(path, key, value).jsonString();
    }

    public static String add(String json, String path, Object obj) {
        return parse(json, path).add(path, obj).jsonString();
    }

    public static String del(String json, String path) {
        return parse(json, path).delete(path).jsonString();
    }

    public static String readStr(String json, String path) {
        return read(json, path, String.class);
    }

    public static Long readLong(String obj, String path) {
        return read(obj, path, Long.class);
    }

    public static Integer readInt(String json, String path) {
        return read(json, path, Integer.class);
    }

    public static Double readDouble(String obj, String path) {
        return read(obj, path, Double.class);
    }

    public static Map<String, Object> readMap(String json, String path) {
        return read(json, path, MAP_TYPE_REF);
    }

    public static <T> List<T> readList(String json, String path, Class<T> clazz) {
        TypeRef<List<T>> typeRef = new TypeRef<List<T>>() {
        };
        return read(json, path, typeRef);
    }

    public static <T> T read(String json, String path, Class<T> clazz) {
        return parse(json, path).read(path, clazz);
    }

    private static <T> T read(String json, String path, TypeRef<T> tr) {
        return parse(json, path).read(path, tr);
    }

    private static DocumentContext parse(String json, String path) {
        return JsonPath
                .using(CONFIG)
                .parse(json);
    }
}
