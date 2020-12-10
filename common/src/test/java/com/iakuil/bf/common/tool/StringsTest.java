package com.iakuil.bf.common.tool;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.core.IsNull.nullValue;

public class StringsTest {

    @Test
    public void should_get_a_correct_sort() {
        assertThat(Strings.parseOrderBy("foo"), is("foo"));
        assertThat(Strings.parseOrderBy("+a,-b,+c"), is("a asc,b desc,c asc"));
        assertThat(Strings.parseOrderBy("+create_time,-user_id"), is("create_time asc,user_id desc"));
        assertThat(Strings.parseOrderBy("a+b-c+"), is("a asc,b desc,c asc"));
    }

    @Test
    public void should_get_null_when_sort_is_blank() {
        assertThat(Strings.parseOrderBy(null), is(nullValue()));
        assertThat(Strings.parseOrderBy(""), is(nullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_an_exception_when_input_wrong_format() {
        Strings.parseOrderBy("a+b-c++");
    }

    @Test
    public void should_get_32_length_uuid_string() {
        assertThat(Strings.getUuidStr(), hasLength(32));
    }
}