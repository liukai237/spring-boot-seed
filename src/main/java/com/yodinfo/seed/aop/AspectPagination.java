package com.yodinfo.seed.aop;

import com.github.pagehelper.PageHelper;
import com.yodinfo.seed.util.StrUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 分页拦截器
 * 如果存在pageNum参数则自动分页
 * 如果存在orderBy参数则自动排序
 * 支持单个字段排序，以及a+b-c+和+a,-b,+c两种组合模式，原SQL排序会被忽略
 */
@Slf4j
@Aspect
@Component
public class AspectPagination {

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
                        PageHelper.startPage(num, size, StrUtils.parseOrderBy(orderBy));
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