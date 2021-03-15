package com.iakuil.bf.shiro;

import com.iakuil.toolkit.PasswordHash;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义密码匹配器
 *
 * <p>同时实现登陆失败重试次数控制
 *
 * @author Kai
 */
@Slf4j
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
    private final Cache<String, AtomicInteger> pwdRetryCount;

    public CustomCredentialsMatcher(CacheManager cacheManager) {
        pwdRetryCount = cacheManager.getCache("pwdRetryCount");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        if (token == null || info == null) {
            return false;
        }

        String username = (String) token.getPrincipal();
        AtomicInteger retryCount = pwdRetryCount.get(username);
        if (retryCount != null && retryCount.incrementAndGet() > 5) {
            // 如果用户登陆失败次数大于5次 抛出锁定用户异常
            throw new LockedAccountException("密码输入错误次数过多，请稍后再试！");
        }

        char[] passwd = (char[]) token.getCredentials();
        String hash = (String) info.getCredentials();
        boolean matches = PasswordHash.validatePassword(passwd, hash);
        if (matches) {
            if (retryCount != null) {
                // 如果密码正确，从缓存中将用户计数清除
                pwdRetryCount.remove(username);
            }
        } else {
            if (retryCount == null) {
                // 第一次输入错误，则在缓存计数1
                pwdRetryCount.put(username, new AtomicInteger(1));
            } else {
                // 连续输入错误，继续+1
                pwdRetryCount.put(username, retryCount);
            }
        }

        return matches;
    }
}
