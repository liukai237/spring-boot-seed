package com.iakuil.seed.common.db;

import com.github.pagehelper.PageHelper;
import com.iakuil.seed.common.BaseDomain;
import com.iakuil.seed.annotation.StartPage;
import com.iakuil.seed.common.tool.BeanMapUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 分页拦截器
 * <p>从参数列表、参数对象属性或者Map参数Key Set中获取分页排序参数。</p><br/>
 * <p>Service层方法添加{@code @StartPage}注解后，如果任意位置存在pageNum则自动分页，
 * 如果存在sort/orderBy则自动排序，重复出现则以最后一个值为准。</p><br/>
 * <p>BTW. 可以通过{@code @StartPage}注解指定分页和排序默认值。</p>
 */
@Aspect
@Component
public class AspectPagination {
    @Around(value = "execution(* com.iakuil.seed.service..*.*(..))")
    public Object aroundMethodWithParam(ProceedingJoinPoint pjd) throws Throwable {
        StartPage sp = pjd.getClass().getAnnotation(StartPage.class);
        if (sp == null) {
            return pjd.proceed();
        }

        String[] names = ((CodeSignature) pjd.getSignature()).getParameterNames();
        Object[] values = pjd.getArgs();

        int pageNum = sp.pageNum();
        int pageSize = sp.pageSize();
        String orderBy = sp.orderBy();
        for (int i = 0; i < names.length; i++) {
            Object name = names[i];
            Object value = values[i];
            if (value == null) {
                continue;
            }

            if (value instanceof Map || value instanceof BaseDomain) {
                Map<String, Object> map = value instanceof BaseDomain ? BeanMapUtils.beanToMap(value) : (Map) value;
                Integer pn = MapUtils.getInteger(map, "pageNum");
                if (pn != null) {
                    pageNum = pn;
                }
                Integer ps = MapUtils.getInteger(map, "pageSize");
                if (ps != null) {
                    pageSize = ps;
                }
                String ob = MapUtils.getString(map, "orderBy", MapUtils.getString(map, "sort"));
                if (StringUtils.isNoneBlank(ob)) {
                    orderBy = ob;
                }
                continue;
            }

            if (value instanceof Integer && "pageNum".equals(name)) {
                pageNum = (Integer) value;
                continue;
            }

            if (value instanceof Integer && "pageSize".equals(name)) {
                pageSize = (Integer) value;
                continue;
            }

            if (value instanceof String && ("orderBy".equals(name) || "sort".equals(name))) {
                orderBy = (String) value;
            }
        }

        if (pageNum != 0) {
            if (StringUtils.isNoneBlank(orderBy)) {
                PageHelper.startPage(pageNum, pageSize, orderBy);
            } else {
                PageHelper.startPage(pageNum, pageSize);
            }
        }

        return pjd.proceed();
    }
}
