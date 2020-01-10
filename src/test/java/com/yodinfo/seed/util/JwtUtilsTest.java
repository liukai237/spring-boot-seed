package com.yodinfo.seed.util;

public class JwtUtilsTest {
//
//    @Test
//    public void testGetIdentity() {
//        String aToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxMzQwMDAwMDAwMCIsImV4cCI6MTU3NTM1ODMwMX0.XlxZXmzxwL5bKojy88GgTYa138anQ_jl_wZMRM_MaYo";
//        String mobile = JwtUtils.getIdentity(aToken);
//        assertThat(mobile, is("13400000000"));
//    }
//
//    @Test
//    public void testSign() {
//        String token = JwtUtils.sign("13400000000");
//        assertThat(token.length(), is(132));
//
//        String token2 = JwtUtils.sign("ow_bdwHkcTeGHsunwhGm9Ofszcn0");
//        assertThat(token2.length(), is(155));
//        String[] parts = token2.split("\\.");
//        assertThat(parts, arrayWithSize(3));
//    }
//
//    @Test
//    public void testVerify() {
//        String token = JwtUtils.sign("13400000000");
//        assertThat(token, notNullValue());
//
//        boolean result = JwtUtils.verify(token);
//        assertThat(result, is(true));
//
//        boolean result2 = JwtUtils.verify("itsAJunk");
//        assertThat(result2, is(false));
//
//        String veryOldToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxMzQwMDAwMDAwMCIsImV4cCI6MTU3NTM1ODMwMX0.XlxZXmzxwL5bKojy88GgTYa138anQ_jl_wZMRM_MaYo";
//        boolean result3 = JwtUtils.verify(veryOldToken);
//        assertThat(result3, is(false));
//    }
}