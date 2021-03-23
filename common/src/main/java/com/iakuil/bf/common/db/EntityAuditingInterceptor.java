package com.iakuil.bf.common.db;

import com.iakuil.bf.common.security.UserDetails;
import com.iakuil.bf.common.security.UserDetailsService;
import com.iakuil.bf.common.tool.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * MyBatis实体类审计插件
 *
 * <p>自动填创建时间、创建人、修改时间和修改人等字段，支持批量操作，支持id回写。
 *
 * @author Kai
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class EntityAuditingInterceptor implements Interceptor {

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
        UserDetailsService userDetailsService = ApplicationContextHolder.getBean(UserDetailsService.class);
        UserDetails currentUser = userDetailsService.getCurrentUser();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (AnnotationUtils.getAnnotation(field, LastModifiedDate.class) != null) {
                field.setAccessible(true);
                field.set(obj, currentDate);
                field.setAccessible(false);
            }
            if (AnnotationUtils.getAnnotation(field, LastModifiedBy.class) != null) {
                field.setAccessible(true);
                field.set(obj, currentUser.getId());
                field.setAccessible(false);
            }
        }
    }

    private void handleUpdateTime(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return;
        }
        Date currentDate = new Date();
        UserDetailsService userDetailsService = ApplicationContextHolder.getBean(UserDetailsService.class);
        UserDetails currentUser = userDetailsService.getCurrentUser();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (AnnotationUtils.getAnnotation(field, CreatedDate.class) != null) {
                field.setAccessible(true);
                field.set(obj, currentDate);
                field.setAccessible(false);
            }
            if (AnnotationUtils.getAnnotation(field, CreatedBy.class) != null) {
                field.setAccessible(true);
                field.set(obj, currentUser.getId());
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