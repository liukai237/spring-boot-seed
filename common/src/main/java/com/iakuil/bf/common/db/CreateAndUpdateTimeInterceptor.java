package com.iakuil.bf.common.db;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * DB创建/修改时间字段自动填充
 *
 * <p>自动填充创建/修改时间以等字段，支持批量操作，支持id回写。
 *
 * @author Kai
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class CreateAndUpdateTimeInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
        if (SqlCommandType.UPDATE == sqlCommandType) {
            // 如果是批量操作
            if (parameter instanceof DefaultSqlSession.StrictMap) {
                List dataList = (List) ((DefaultSqlSession.StrictMap) parameter).get("list");
                for (Object obj : dataList) {
                    handlCreateTime(obj);
                }
            } else {
                handlCreateTime(parameter);
            }
        } else if (SqlCommandType.INSERT == sqlCommandType) {
            if (parameter instanceof DefaultSqlSession.StrictMap) {
                List dataList = (List) ((DefaultSqlSession.StrictMap) parameter).get("list");
                for (Object obj : dataList) {
                    handlCreateTime(obj);
                    handlUpdateTime(obj);
                }
            } else {
                handlCreateTime(parameter);
                handlUpdateTime(parameter);
            }
        } else {
            // do nothing
        }
        return invocation.proceed();
    }

    private void handlCreateTime(Object obj) throws IllegalAccessException {
        Date currentDate = new Date();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (AnnotationUtils.getAnnotation(field, LastModifiedDate.class) != null) {
                field.setAccessible(true);
                field.set(obj, currentDate);
                field.setAccessible(false);
            }
        }
    }

    private void handlUpdateTime(Object obj) throws IllegalAccessException {
        Date currentDate = new Date();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (AnnotationUtils.getAnnotation(field, CreatedDate.class) != null) {
                field.setAccessible(true);
                field.set(obj, currentDate);
                field.setAccessible(false);
            }
        }
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