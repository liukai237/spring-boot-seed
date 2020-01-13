package com.yodinfo.seed.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class StrUtilsTest {

    @Test
    public void should_get_a_correct_sort() {
        assertThat(StrUtils.parseOrderBy("foo"), is("foo"));
        assertThat(StrUtils.parseOrderBy("+a,-b,+c"), is("a asc,b desc,c asc"));
        assertThat(StrUtils.parseOrderBy("+create_time,-user_id"), is("create_time asc,user_id desc"));
        assertThat(StrUtils.parseOrderBy("a+b-c+"), is("a asc,b desc,c asc"));
    }

    @Test
    public void should_get_null_when_sort_is_blank() {
        assertThat(StrUtils.parseOrderBy(null), is(nullValue()));
        assertThat(StrUtils.parseOrderBy(""), is(nullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_an_exception_when_input_wrong_format() {
        StrUtils.parseOrderBy("a+b-c++");
    }
}