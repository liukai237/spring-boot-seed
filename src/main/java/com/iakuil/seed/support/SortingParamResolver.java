package com.iakuil.seed.support;

import com.iakuil.seed.constant.Constant;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 排序参数自动转换
 * <p>自动将a+b-c+或者+a,-b,+c格式的Request Param转换为SQL样式的排序方式。</p>
 *
 * @see Strings
 */
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