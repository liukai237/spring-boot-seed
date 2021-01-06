/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 342252328@qq.com
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.iakuil.bf.common.db;

import com.iakuil.bf.common.tool.JsonUtils;
import com.iakuil.bf.common.tool.PluginUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

/**
 * <p>MyBatis乐观锁插件
 * <p>MyBatis Optimistic Locker Plugin
 *
 * @author Kai
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        @Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class OptimisticLockerInterceptor implements Interceptor {

    private static final Log log = LogFactory.getLog(OptimisticLockerInterceptor.class);
    private String versionColumn;

    @Override
    public void setProperties(Properties properties) {
        versionColumn = properties.getProperty("versionColumn", "version");
        if (versionColumn == null || versionColumn.trim().length() == 0) {
            versionColumn = "version";
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object intercept(Invocation invocation) throws Exception {

        String interceptMethod = invocation.getMethod().getName();
        if ("prepare".equals(interceptMethod)) {
            StatementHandler routingHandler = (StatementHandler) PluginUtils.processTarget(invocation.getTarget());
            MetaObject routingMeta = SystemMetaObject.forObject(routingHandler);
            MetaObject hm = routingMeta.metaObjectForProperty("delegate");

            boolean locker = VersionLockerResolver.resolve(hm, versionColumn);
            if (!locker) {
                return invocation.proceed();
            }

            String originalSql = (String) hm.getValue("boundSql.sql");
            StringBuilder builder = new StringBuilder(originalSql);
            builder.append(" AND ");
            builder.append(versionColumn);
            builder.append(" = ?");
            hm.setValue("boundSql.sql", builder.toString());

        } else if ("setParameters".equals(interceptMethod)) {
            ParameterHandler handler = (ParameterHandler) PluginUtils.processTarget(invocation.getTarget());
            MetaObject hm = SystemMetaObject.forObject(handler);

            boolean locker = VersionLockerResolver.resolve(hm, versionColumn);
            if (!locker) {
                return invocation.proceed();
            }

            BoundSql boundSql = (BoundSql) hm.getValue("boundSql");
            Object parameterObject = boundSql.getParameterObject();
            if (parameterObject instanceof MapperMethod.ParamMap<?>) {
                MapperMethod.ParamMap<?> paramMap = (MapperMethod.ParamMap<?>) parameterObject;
                if (!paramMap.containsKey(versionColumn)) {
                    throw new TypeException("All the primitive type parameters must add MyBatis's @Param Annotation");
                }
            }

            Configuration configuration = ((MappedStatement) hm.getValue("mappedStatement")).getConfiguration();
            MetaObject pm = configuration.newMetaObject(parameterObject);
            Object value = pm.getValue(versionColumn);
            ParameterMapping versionMapping = new ParameterMapping.Builder(configuration, versionColumn, Object.class).build();
            TypeHandler typeHandler = versionMapping.getTypeHandler();
            JdbcType jdbcType = versionMapping.getJdbcType();

            if (value == null && jdbcType == null) {
                jdbcType = configuration.getJdbcTypeForNull();
            }

            int versionLocation = boundSql.getParameterMappings().size() + 1;
            try {
                PreparedStatement ps = (PreparedStatement) invocation.getArgs()[0];
                typeHandler.setParameter(ps, versionLocation, value, jdbcType);
            } catch (TypeException | SQLException e) {
                throw new TypeException("set parameter 'version' faild, Cause: " + e, e);
            }

            if (!Objects.equals(value.getClass(), Long.class) && Objects.equals(value.getClass(), long.class) && log.isDebugEnabled()) {
                log.error("[Optimistic Locker Plugin Error] property type error, the type of version property must be Long or long.");
            }

            // increase version
            pm.setValue(versionColumn, (long) value + 1);
        } else if ("update".equals(interceptMethod)) {
            MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
            SqlCommandType sct = ms.getSqlCommandType();
            if (sct.equals(SqlCommandType.UPDATE)) {
                int result = (int) invocation.proceed();
                if (result == 0) {
                    Object param = invocation.getArgs()[1];
                    BoundSql boundSql = ms.getBoundSql(param);
                    String sql = boundSql.getSql();
                    String paramJson = JsonUtils.bean2Json(param);
                    throw new IllegalStateException("[触发乐观锁，更新失败], 失败SQL: " + sql + ", 参数: " + paramJson);
                }
                return result;
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor || target instanceof StatementHandler || target instanceof ParameterHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

}