package com.yodinfo.seed.bo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class LoginInfo {

    @NotEmpty
    @JsonProperty("username")
    @JsonAlias({"account", "mobile", "uid"})
    private String principal;

    @NotEmpty
    @JsonProperty("password")
    @JsonAlias({"passWd", "passwd", "access_token"})
    private String credential;

    // 账号类型
    private AccountType accountType;

    // 凭证类型
    private PassphraseType passphraseType;

    // 登陆设备
    private DeviceType deviceType;

    @AllArgsConstructor
    private enum AccountType {
        USER_NAME(0), // 用户名
        MOBILE_NUMBER(1), // 手机号码
        EMAIL(2); // email

        Integer id;

        @JsonValue
        public Integer getId() {
            return id;
        }
    }

    @AllArgsConstructor
    private enum PassphraseType {
        PASSWORD(0), // 密码
        VERIFICATION_CODE(1), // 手机验证码
        OAUTH2(2); // 第三方登录

        Integer id;

        @JsonValue
        public Integer getId() {
            return id;
        }
    }

    @AllArgsConstructor
    private enum DeviceType {
        WEB_BROWSER(0), // 普通浏览器
        WX_BROWSER(1), // 微信浏览器
        IOS(2), // 苹果
        ANDROID(3), // 安卓
        MINI_PROGRAM(4); // 微信小程序

        Integer id;

        @JsonValue
        public Integer getId() {
            return id;
        }
    }
}