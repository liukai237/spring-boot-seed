package com.iakuil.bf.common.db;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 实体类创建/修改时间字段自动填充
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
            Iterable entities = getAsIterable(parameter);
            if (entities == null) {
                handleUpdateTime(parameter);
            } else {
                // 如果是批量操作
                for (Object entity : entities) {
                    handleUpdateTime(entity);
                }
            }
        } else if (SqlCommandType.INSERT == sqlCommandType) {
            Iterable entities = getAsIterable(parameter);
            if (entities == null) {
                handleCreateTime(parameter);
                handleUpdateTime(parameter);
            } else {
                // 如果是批量操作
                for (Object entity : entities) {
                    handleCreateTime(entity);
                    handleUpdateTime(entity);
                }
            }
        } else {
            // do nothing
        }
        return invocation.proceed();
    }

    private Iterable getAsIterable(Object parameter) {
        if (parameter instanceof DefaultSqlSession.StrictMap || parameter instanceof MapperMethod.ParamMap) {
            Iterable entities = null;
            if (((Map) parameter).containsKey("collection")) {
                entities = (Iterable) ((Map) parameter).get("collection");
            } else if (((Map) parameter).containsKey("array")) {
                entities = Arrays.stream((Object[]) ((Map) parameter).get("array")).collect(Collectors.toList());

            } else {
                // do nothing
            }

            return entities;
        }

        return null;
    }

    private void handleCreateTime(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return;
        }
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

    private void handleUpdateTime(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return;
        }
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