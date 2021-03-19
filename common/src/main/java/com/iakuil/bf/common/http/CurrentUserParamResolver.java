package com.iakuil.bf.common.http;

import com.iakuil.bf.common.security.UserDetails;
import com.iakuil.bf.common.security.UserDetailsService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;

/**
 * 当前登录用户参数解析（试验功能）
 *
 * @author Kai
 * @date 2021/3/19 16:54
 **/
public class CurrentUserParamResolver implements HandlerMethodArgumentResolver {

    @Resource
    private UserDetailsService userDetailsService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserDetails.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return userDetailsService.getCurrentUser();
    }
}
