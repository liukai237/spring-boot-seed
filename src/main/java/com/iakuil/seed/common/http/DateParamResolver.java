package com.iakuil.seed.common.http;

import com.iakuil.seed.annotation.EndDate;
import com.iakuil.seed.annotation.StartDate;
import com.iakuil.seed.common.tool.Dates;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Date;
import java.util.Objects;

/**
 * Request Query日期参数处理
 */
public class DateParamResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Date.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String[] dateValues = webRequest.getParameterMap().values().stream().filter(item -> !Objects.isNull(item)).findFirst().orElse(null);
        Date realDate = null;
        if (dateValues != null && dateValues.length > 0) {
            String dateStr = dateValues[0];
            if (parameter.hasParameterAnnotation(StartDate.class)) {
                realDate = Dates.getDayStart(dateStr);
            } else if (parameter.hasParameterAnnotation(EndDate.class)) {
                realDate = Dates.getDayEnd(dateStr);
            } else {
                realDate = Dates.parseDate(dateStr);
            }
        }

        return realDate;
    }
}
