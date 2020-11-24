package com.iakuil.seed.common.tool;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

class BeanUtilsTest {

    @Test
    void should_copy_non_null_properties() {
        Foo foo = new Foo();
        foo.setName("Harry");

        Bar bar = BeanUtils.copy(foo, Bar.class);
        assertThat(foo.getName(), is(bar.getName()));
    }

    @Test
    void should_copy_necessary_properties() {
        Bar bar = new Bar();
        bar.setName("Zhang San");
        bar.setAge(30);
        bar.setAddr("China");

        Foo foo = BeanUtils.copy(bar, Foo.class);
        assertThat(foo.getName(), is(bar.getName()));
        assertThat(foo.getAge(), is(30));
    }

    @Test
    void should_copy_non_null_and_the_same_type_properties() {
        Foo foo = new Foo();
        foo.setName("Tom");
        foo.setAge(18);

        Baz baz = BeanUtils.copy(foo, Baz.class);
        assertThat(foo.getName(), is(baz.getName()));
        assertThat(baz.getAge(), is(nullValue()));
    }

    @Test
    void should_copy_two_items() {
        Foo foo = new Foo();
        foo.setName("Page");
        foo.setAge(18);

        Foo bar = new Foo();
        bar.setName("George");
        bar.setAge(8);

        List<Bar> family = BeanUtils.copyMany(Arrays.asList(foo, bar), Bar.class);
        assertThat(family, hasSize(2));
    }

    @Data
    private static class Foo {
        private String name;
        private Integer age;
    }

    @Data
    private static class Bar {
        private String name;
        private Integer age;
        private String addr;
    }

    @Data
    private static class Baz {
        private String name;
        private String age;
    }
}