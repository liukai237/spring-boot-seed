package com.yodinfo.seed.util;

import com.github.pagehelper.PageHelper;
import com.yodinfo.seed.bo.PagingParam;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 分页拦截器
 * 如果DAO层方法存在PagingParam对象参数则自动实现物理分页或者简单排序
 */
//@Aspect
//@Component
public class AspectPagination {
    private Logger LOGGER = LoggerFactory.getLogger(AspectPagination.class);

    @Around(value = "execution(* com.yodinfo.seed.dao..*.*(..))")
    public Object aroundMethodWithParam(ProceedingJoinPoint pjd) throws Throwable {
        Object[] params = pjd.getArgs();

        if (ArrayUtils.isEmpty(params)) {
            return pjd.proceed();
        }

        for (Object param : params) {
            if (param instanceof PagingParam) {
                LOGGER.debug("<<<<<<<<Aspect page start>>>>>>>>");
                PagingParam pp = (PagingParam) param;
                Integer pageNum = pp.getPageNum();
                Integer pageSize = pp.getPageSize();
                String orderBy = pp.getOrderBy();
                if (orderBy == null || orderBy.equals("")) {
                    LOGGER.debug("[pageNum:{}, pageSize:{}]", pageNum, pageSize);
                    PageHelper.startPage(pageNum, pageSize);
                } else {
                    LOGGER.debug("[pageNum:{}, pageSize:{}, orderBy:{}]", pageNum, pageSize, orderBy);
                    PageHelper.startPage(pageNum, pageSize, orderBy);
                }

                break;
            }
        }

        return pjd.proceed();
    }
}