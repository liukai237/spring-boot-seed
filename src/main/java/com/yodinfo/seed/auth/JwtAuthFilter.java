package com.yodinfo.seed.auth;

import com.yodinfo.seed.constant.RedisKeyConstant;
import com.yodinfo.seed.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public class JwtAuthFilter extends AuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info(request.getMethod());
        log.info(request.getRequestURL().toString());
        String token = request.getHeader(RedisKeyConstant.ACCESS_TOKEN);
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(""); // TODO 处理异常
        }
        JwtToken jwtToken = new JwtToken(token);

        // 委托给Realm进行登录
        getSubject(servletRequest, servletResponse).login(jwtToken);
        return true;
    }
}
