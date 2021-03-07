package com.iakuil.bf.common.http;

import com.iakuil.bf.common.constant.SysConstant;
import com.iakuil.bf.common.tool.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 排序参数自动转换
 *
 * <p>自动将a+b-c+或者+a,-b,+c格式的Request Param转换为SQL样式的排序方式。
 *
 * @author Kai
 */
public class SortingParamResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        String pName = methodParameter.getParameterName();
        return SysConstant.DEFAULT_SORT_PARAM.equals(pName);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Object sortObj = nativeWebRequest.getParameterMap().get(SysConstant.DEFAULT_SORT_PARAM);
        return sortObj == null ? null : Strings.parseOrderBy(Strings.toUnderlineCase(((String[]) sortObj)[0]));
    }
}