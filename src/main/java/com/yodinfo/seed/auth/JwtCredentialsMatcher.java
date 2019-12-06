package com.yodinfo.seed.auth;

import com.yodinfo.seed.constant.RedisKeyConstant;
import com.yodinfo.seed.exception.BusinessException;
import com.yodinfo.seed.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class JwtCredentialsMatcher implements CredentialsMatcher {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Matcher中直接调用工具包中的verify方法即可
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        JwtToken JwtToken = (JwtToken) token;
        String accessToken = (String) JwtToken.getPrincipal();
        String identityId = (String) JwtToken.getCredentials();

        /**
         * 用户主动退出
         */
        if (redisTemplate.hasKey(RedisKeyConstant.JWT_REFRESH_TOKEN_BLACKLIST + accessToken)) {
            throw new BusinessException("");
        }
        /**
         * 存在这个key 说明接口已经刷新过还没有来得及更换新的 token
         */
        if (redisTemplate.hasKey(RedisKeyConstant.JWT_REFRESH_STATUS + accessToken)) {
            return true;
        }
        /**
         * token 已经过期 或者主动去刷新
         */
        if (JwtUtils.verify(accessToken)) {
            //TODO...
        }
        /**
         * 判断这个登录用户是否要主动去刷新
         *
         */
        if (redisTemplate.hasKey(RedisKeyConstant.JWT_REFRESH_KEY + identityId)) {
            /**
             * 是否存在刷新的标识
             */
            if (!redisTemplate.hasKey(RedisKeyConstant.JWT_REFRESH_IDENTIFICATION + accessToken)) {
                //TODO...
            }
        }

        return true;
    }
}