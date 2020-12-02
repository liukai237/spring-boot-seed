package com.iakuil.bf.service.util;


import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class PasswordHashTest {

    @Test
    public void testValidatePassword() throws Exception {
        String pwdHash1 = PasswordHash.createHash("Bar");
        assertThat(pwdHash1, notNullValue());
        String pwdHash2 = PasswordHash.createHash("Bar");
        assertThat(pwdHash2, notNullValue());

        assertThat(pwdHash1, not(pwdHash2));

        assertThat(PasswordHash.validatePassword("Bar", pwdHash1), is(true));
        assertThat(PasswordHash.validatePassword("Bar", pwdHash2), is(true));
    }
}