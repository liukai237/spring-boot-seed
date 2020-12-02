package com.iakuil.bf.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "wechat")
public class WxSettings {
    private String alias;
    private String appid;
    private String appsecret;
}
