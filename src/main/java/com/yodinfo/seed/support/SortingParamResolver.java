package com.yodinfo.seed.support;

import com.yodinfo.seed.constant.Constant;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SortingParamResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        String pName = methodParameter.getParameterName();
        return Constant.DEFAULT_SORT_FIELD.equals(pName);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Object sortObj = nativeWebRequest.getParameterMap().getOrDefault(Constant.DEFAULT_SORT_FIELD, ArrayUtils.EMPTY_STRING_ARRAY);
        return Strings.parseOrderBy(((String[]) sortObj)[0]);
    }
}