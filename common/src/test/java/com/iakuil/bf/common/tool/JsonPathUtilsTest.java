package com.iakuil.bf.common.tool;

import com.jayway.jsonpath.spi.mapper.MappingException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class JsonPathUtilsTest {

    @Test
    void should_add_object_to_json_array_suitably() {
        String json = "[{\"name\":\"Tom\"}]";
        String afterAdd = JsonPathUtils.add(json, "$", "{\"name\":\"Jack\"}");
        assertThat(afterAdd, containsString("Jack"));
    }

    @Test
    void should_append_element_property() {
        String json = "{\"name\":\"Tom\"}";
        String afterPut = JsonPathUtils.put(json, "$", "age", 10);
        assertThat(afterPut, containsString("10"));
    }

    @Test
    void should_replace_name_field_with_new_value() {
        String json = "{\"name\":\"Tom\"}";
        String afterAdd = JsonPathUtils.put(json, "$", "name", "Jack");
        assertThat(afterAdd, containsString("Jack"));
        assertThat(afterAdd, not(containsString("Tom")));
    }

    @Test
    void should_remove_name_field() {
        String json = "{\"name\":\"Tom\",\"age\":11,\"addr\":\"China\"}";
        String afterAdd = JsonPathUtils.del(json, "$.name");
        assertThat(afterAdd, not(containsString("Tom")));
    }

    @Test
    void should_get_null_when_field_is_not_exist() {
        String json = "{\"name\":\"Tom\"}";
        assertThat(JsonPathUtils.readStr(json, "$.xxx"), nullValue());
    }

    @Test
    void should_get_correct_name_when_json_is_valid() {
        String json = "{\"name\":\"Tom\"}";
        assertThat(JsonPathUtils.readStr(json, "$.name"), is("Tom"));
    }

    @Test
    void should_get_correct_age_as_string_when_json_is_numeric() {
        String json = "{\"age\":11}";
        assertThat(JsonPathUtils.readStr(json, "$.age"), is("11"));
    }

    @Test
    void should_get_correct_age_when_json_is_valid() {
        String json = "{\"age\":11}";
        assertThat(JsonPathUtils.readInt(json, "$.age"), is(11));
    }

    @Test
    void should_get_correct_map_when_json_is_valid() {
        String json = "{\"name\":\"Tom\"}";
        Map<String, Object> map = JsonPathUtils.readMap(json, "$");
        assertThat(map, notNullValue());
        assertThat(map, hasKey("name"));
        assertThat(map, hasValue("Tom"));
    }

    @Test
    void should_get_correct_class_when_json_fields_is_less() {
        String json = "{\"name\":\"Tom\"}";
        Foo obj = JsonPathUtils.read(json, "$", Foo.class);
        assertThat(obj, notNullValue());
        assertThat(obj, hasProperty("name"));
        assertThat(obj.getName(), is("Tom"));
    }

    @Test
    void should_get_exception_class_when_json_fields_is_redundant() {
        String json = "{\"name\":\"Tom\",\"age\":11,\"addr\":\"China\"}";
        MappingException exception = assertThrows(MappingException.class,
                () -> JsonPathUtils.read(json, "$", Foo.class));
        assertTrue(exception.getMessage().contains("Unrecognized field \"addr\""));
    }

    @Test
    void should_get_correct_class_when_json_is_nesting() {
        String json = "{\"foo\":{\"name\":\"Tom\",\"age\":11},\"addr\":\"China\"}";
        Foo obj = JsonPathUtils.read(json, "$.foo", Foo.class);
        assertThat(obj, notNullValue());
        assertThat(obj, hasProperty("name"));
        assertThat(obj.getName(), is("Tom"));
    }

    @Test
    void should_get_correct_list_when_json_is_nesting() {
        String json = "[{\"name\":\"Tom\",\"age\":11},{\"name\":\"Harry\",\"age\":22}]";
        List<Foo> list = JsonPathUtils.readList(json, "$", Foo.class);
        assertThat(list, hasSize(2));
        System.out.println();
    }

    private static class Foo {
        private String name;
        private Integer age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}