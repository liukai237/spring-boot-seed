package com.yodinfo.seed.util;

import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 分页拦截器
 * 如果存在pageNum参数则自动分页
 * 如果存在orderBy参数则自动排序
 * 排序仅支持一个字段，原SQL排序会被忽略
 */
@Aspect
@Component
public class AspectPagination {
    private Logger LOGGER = LoggerFactory.getLogger(AspectPagination.class);

    @Around(value = "execution(* com.yodinfo.seed.dao..*.*(..))")
    public Object aroundMethodWithParam(ProceedingJoinPoint pjd) throws Throwable {
        Object[] params = pjd.getArgs();

        if (ArrayUtils.isEmpty(params)) {
            return pjd.proceed();
        }

        for (Object param : params) {
            if (param instanceof Map) {
                Map map = (Map) param;
                Integer num = MapUtils.getInteger(map, "pageNum");
                Integer size = MapUtils.getInteger(map, "pageSize"); // zero means no paging!
                String orderBy = MapUtils.getString(map, "orderBy");
                if (size != null && size != 0) {
                    if (StringUtils.isNoneBlank(orderBy)) {
                        PageHelper.startPage(num, size, orderBy);
                    } else {
                        PageHelper.startPage(num, size);
                    }
                }

                break;
            }
        }

        return pjd.proceed();
    }
}