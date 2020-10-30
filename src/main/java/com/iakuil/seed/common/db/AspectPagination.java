package com.iakuil.seed.common.db;

import com.github.pagehelper.PageHelper;
import com.iakuil.seed.annotation.StartPage;
import com.iakuil.seed.common.BaseDomain;
import com.iakuil.seed.common.tool.BeanMapUtils;
import com.iakuil.seed.constant.SysConstant;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
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
        StartPage sp = ((MethodSignature) pjd.getSignature()).getMethod().getAnnotation(StartPage.class);
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
                Integer pn = MapUtils.getInteger(map, SysConstant.DEFAULT_PAGE_NUM_FIELD);
                if (pn != null) {
                    pageNum = pn;
                }
                Integer ps = MapUtils.getInteger(map, SysConstant.DEFAULT_PAGE_SIZE_FIELD);
                if (ps != null) {
                    pageSize = ps;
                }
                String ob = MapUtils.getString(map, SysConstant.DEFAULT_ORDER_FIELD, MapUtils.getString(map, SysConstant.DEFAULT_SORT_FIELD));
                if (StringUtils.isNoneBlank(ob)) {
                    orderBy = ob;
                }
                continue;
            }

            if (value instanceof Integer && SysConstant.DEFAULT_PAGE_NUM_FIELD.equals(name)) {
                Integer pn = (Integer) value;
                if (pn > 0) {
                    pageNum = pn;
                }
                continue;
            }

            if (value instanceof Integer && SysConstant.DEFAULT_PAGE_SIZE_FIELD.equals(name)) {
                Integer ps = (Integer) value;
                if (ps > 0) {
                    pageSize = ps;
                }
                continue;
            }

            if (value instanceof String && (SysConstant.DEFAULT_ORDER_FIELD.equals(name) || SysConstant.DEFAULT_SORT_FIELD.equals(name))) {
                String ob = (String) value;
                if (StringUtils.isNoneBlank(ob)) {
                    orderBy = ob;
                }
            }
        }

        // 限制单次查询最大返回数量
        pageSize = (pageSize == 0 || pageSize > SysConstant.MAX_PAGE_SIZE) ? SysConstant.MAX_PAGE_SIZE : pageSize;
        if (StringUtils.isNoneBlank(orderBy)) {
            PageHelper.startPage(pageNum, pageSize, orderBy);
        } else {
            PageHelper.startPage(pageNum, pageSize);
        }
        return pjd.proceed();
    }
}
