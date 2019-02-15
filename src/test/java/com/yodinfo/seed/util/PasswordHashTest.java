package com.yodinfo.seed.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PasswordHashTest {

    @Test
    public void testValidatePassword() throws Exception {
        String pwdHash1 = PasswordHash.createHash("Foo");
        assertThat(pwdHash1, notNullValue());
        String pwdHash2 = PasswordHash.createHash("Foo");
        assertThat(pwdHash2, notNullValue());

        assertThat(pwdHash1, not(pwdHash2));

        assertThat(PasswordHash.validatePassword("Foo", pwdHash1), is(true));
        assertThat(PasswordHash.validatePassword("Foo", pwdHash2), is(true));
    }
}