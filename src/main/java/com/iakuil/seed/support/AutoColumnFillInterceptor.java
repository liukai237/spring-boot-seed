package com.iakuil.seed.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

/**
 * DB字段自动填充
 * <p>自动填充create_time，update_time等字段</p>
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class AutoColumnFillInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
        Field[] fields = parameter.getClass().getDeclaredFields();
        Date currentDate = new Date();
        if (SqlCommandType.UPDATE == sqlCommandType) {
            for (Field field : fields) {
                if (AnnotationUtils.getAnnotation(field, LastModifiedDate.class) != null) {
                    field.setAccessible(true);
                    field.set(parameter, currentDate);
                    field.setAccessible(false);
                }
            }
        } else if (SqlCommandType.INSERT == sqlCommandType) {
            for (Field field : fields) {
                if (AnnotationUtils.getAnnotation(field, CreatedDate.class) != null) {
                    field.setAccessible(true);
                    field.set(parameter, currentDate);
                    field.setAccessible(false);
                }
                if (AnnotationUtils.getAnnotation(field, LastModifiedDate.class) != null) {
                    field.setAccessible(true);
                    field.set(parameter, currentDate);
                    field.setAccessible(false);
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }
}