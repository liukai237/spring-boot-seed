package com.iakuil.seed.util;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import lombok.experimental.UtilityClass;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 使用Json Path的方式访问JSON
 */
@UtilityClass
public class JsonPathUtils {

    public static String readStr(String json, String path) {
        return read(json, path, String.class);
    }

    public static String readStr(Object obj, String path) {
        return read(JsonUtils.bean2Json(obj), path, String.class);
    }

    public static Integer readInt(String json, String path) {
        return read(json, path, Integer.class);
    }

    public static Integer readInt(Object obj, String path) {
        return read(JsonUtils.bean2Json(obj), path, Integer.class);
    }

    public static Map<String, Object> readMap(String json, String path) {
        TypeRef<Map<String, Object>> typeRef = new TypeRef<Map<String, Object>>() {
        };
        return read(json, path, typeRef);
    }

    public static <T> List<T> readList(String json, String path, Class<T> clazz) {
        TypeRef<List<T>> typeRef = new TypeRef<List<T>>() {
        };
        return read(json, path, typeRef);
    }

    public static <T> T read(String json, String path, Class<T> clazz) {
        return JsonPath
                .using(getConfiguration())
                .parse(json)
                .read(path, clazz);
    }

    private static <T> T read(String json, String path, TypeRef<T> tr) {
        return JsonPath
                .using(getConfiguration())
                .parse(json)
                .read(path, tr);
    }

    private static Configuration getConfiguration() {
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

        return Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
    }
}
