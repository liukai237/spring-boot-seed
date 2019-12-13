package com.yodinfo.seed.auth;

import com.yodinfo.seed.constant.RedisKeyConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
public class JwtAuthFilter extends AuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info(request.getMethod());
        log.info(request.getRequestURL().toString());
        String token = request.getHeader(RedisKeyConstant.ACCESS_TOKEN);
        if (StringUtils.isEmpty(token)) {
            genErrorMsg(servletResponse, "missing token!");
            return false;
        }
        if (!token.contains("Bearer:")) {
            genErrorMsg(servletResponse, "token should begin with Bearer:!");
            return false;
        }
        JwtToken jwtToken = new JwtToken(token.split("^Bearer:")[1].trim());

        try {
            getSubject(servletRequest, servletResponse).login(jwtToken);
        } catch (Exception e) {
            log.error("[AUTH ERROR]", e);
            genErrorMsg(servletResponse, null);
            return false;
        }
        return true;
    }

    private void genErrorMsg(ServletResponse servletResponse, String msg) throws Exception {
        String m = StringUtils.defaultIfBlank(msg, "UNAUTHORIZED");
        HttpServletResponse httpResponse = WebUtils.toHttp(servletResponse);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setStatus(HttpStatus.OK.value());
        PrintWriter writer = httpResponse.getWriter();
        writer.write("{\"code\": 100401, \"message\": \"" + m + "\"}");
    }
}