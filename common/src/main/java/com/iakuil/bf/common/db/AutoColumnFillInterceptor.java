package com.iakuil.bf.common.db;

import com.dangdang.ddframe.rdb.sharding.id.generator.self.CommonSelfIdGenerator;
import com.iakuil.bf.common.annotation.LogicDelete;
import com.iakuil.bf.common.tool.ApplicationContextHolder;
import com.iakuil.bf.common.tool.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

/**
 * DB字段自动填充
 * <p>自动填充创建/修改时间以及逻辑删除等字段，支持批量操作，支持id回写。</p>
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
                if (AnnotationUtils.getAnnotation(field, LastModifiedBy.class) != null) {
                    //TODO...
                }
            }
        } else if (SqlCommandType.INSERT == sqlCommandType) {
            for (Field field : fields) {
                if (AnnotationUtils.getAnnotation(field, Id.class) != null
                        && AnnotationUtils.getAnnotation(field, GeneratedValue.class) == null
                        && AnnotationUtils.getAnnotation(field, KeySql.class) == null) {
                    field.setAccessible(true);
                    if (field.get(parameter) == null) {
                        if (field.getType().equals(String.class)) {
                            field.set(parameter, Strings.getUuidStr());
                        } else if (field.getType().equals(Long.class)) {
                            field.set(parameter, ApplicationContextHolder.getBean(CommonSelfIdGenerator.class).generateId());
                        } else {
                            // do nothing
                        }
                    }

                    field.setAccessible(false);
                }
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
                if (AnnotationUtils.getAnnotation(field, CreatedBy.class) != null) {
                    //TODO...
                }
            }
        } else if (SqlCommandType.DELETE == sqlCommandType) {
            for (Field field : fields) {
                if (AnnotationUtils.getAnnotation(field, LogicDelete.class) != null) {
                    field.setAccessible(true);
                    if (field.getType().equals(String.class)) {
                        field.set(parameter, "1");
                    } else if (field.getType().equals(Long.class) || field.getType().equals(Integer.class)) {
                        field.set(parameter, 1);
                    } else {
                        // do nothing
                    }
                    field.setAccessible(false);
                }
            }
        } else {
            // do nothing
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