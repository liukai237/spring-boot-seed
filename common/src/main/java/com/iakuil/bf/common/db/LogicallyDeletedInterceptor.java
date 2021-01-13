package com.iakuil.bf.common.db;

import com.alibaba.druid.DbType;
import com.iakuil.bf.common.tool.PluginUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Arrays;
import java.util.Properties;

/**
 * MyBatis逻辑删除插件
 *
 * <p>以下两种情况会触发本插件：</p>
 * <ol>
 * <li> 定义在CrudMapper中的通用方法，并且对应的Entity中有逻辑删除字段。
 * <li> 定义在XML Mapper中的方法，ResultMap对应的Entity中有逻辑删除字段。
 * </ol>
 *
 * <p>试验功能</p>
 *
 * @author Kai
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),})
public class LogicallyDeletedInterceptor implements Interceptor {
    private static int MAPPED_STATEMENT_INDEX = 0;

    private String logicDeleteField;
    private String logicDeleteValue;
    private String logicNotDeleteValue;
    private String dbType;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[MAPPED_STATEMENT_INDEX];
        // 触发条件
        if (hasLogicDeleted(mappedStatement.getId())
                || Arrays.stream(mappedStatement.getResultMaps().get(0).getType().getDeclaredFields()).anyMatch(item -> item.getName().equals(logicDeleteField))) {
            LogicallyDeletedDynamicSqlSource deletedDynamicSqlSource = new LogicallyDeletedDynamicSqlSource(DbType.of(dbType), mappedStatement.getSqlSource(), logicDeleteField, logicDeleteValue, logicNotDeleteValue);
            PluginUtils.setFieldValue(mappedStatement, "sqlSource", deletedDynamicSqlSource);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        logicDeleteField = properties.getProperty("logicDeleteField");
        logicDeleteValue = properties.getProperty("logicDeleteValue");
        logicNotDeleteValue = properties.getProperty("logicNotDeleteValue");
        dbType = properties.getProperty("dbType");
    }

    private boolean hasLogicDeleted(String id) {
        return PluginUtils.isFieldMapped(StringUtils.substringBeforeLast(id, "."), logicDeleteField);
    }
}
