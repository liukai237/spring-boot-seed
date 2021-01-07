package com.iakuil.bf.common.db;

import com.alibaba.druid.DbType;
import com.iakuil.bf.common.tool.PluginUtils;
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
 * @author Kai
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),})
public class LogicallyDeletedInterceptor implements Interceptor {

    private String logicDeleteField;
    private String logicDeleteValue;
    private String logicNotDeleteValue;
    private String dbType;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        if (Arrays.stream(mappedStatement.getResultMaps().get(0).getType().getDeclaredFields()).anyMatch(item -> item.getName().equals(logicDeleteField))) {
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
}
