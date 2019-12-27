package com.yodinfo.seed.service;

import com.yodinfo.seed.auth.OAuth2Token;
import com.yodinfo.seed.auth.SmsToken;
import com.yodinfo.seed.constant.AuthSchema;
import com.yodinfo.seed.constant.DeviceType;
import com.yodinfo.seed.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientAuthService {

    public void signIn(String uid, String pass, String code, DeviceType device, AuthSchema schema) {
        AuthenticationToken token;
        switch (schema) {
            case PASSWD:
                token = new UsernamePasswordToken(uid, pass);
                break;
            case SMS:
                token = new SmsToken(uid, pass, code);
                break;
            case OAUTH2: //TODO 从数据库读取第三方Auth2.0接口配置
                token = new OAuth2Token("", "", pass);
                break;
            default:
                throw new BusinessException("invalid auth schema"); // should not been happened
        }

        SecurityUtils.getSubject().login(token);
    }

    public void signOut() {
        SecurityUtils.getSubject().logout();
    }
}