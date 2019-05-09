package com.yodinfo.seed.config;

import com.github.pagehelper.PageInterceptor;
import com.yodinfo.seed.util.AutoEnumTypeHandler;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "com.yodinfo.seed.dao.*", sqlSessionFactoryRef = "primarySessionFactory")
@EnableTransactionManagement
public class DbConfig {

    @Bean(name = "primarySessionFactory")
    @Primary
    public SqlSessionFactory primarySessionFactory(@Qualifier("hikariDataSource") DataSource hikariDataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(hikariDataSource);

        // 分页插件
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        pageInterceptor.setProperties(properties);
        bean.setPlugins(new PageInterceptor[]{pageInterceptor});

        SqlSessionFactory factory = bean.getObject();
        if (factory != null) { // 注册自定义枚举转换器
            org.apache.ibatis.session.Configuration config = factory.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = config.getTypeHandlerRegistry();
            typeHandlerRegistry.setDefaultEnumTypeHandler(AutoEnumTypeHandler.class);
        }

        return factory;
    }

    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(@Qualifier("hikariDataSource") DataSource hikariDataSource) {
        return new DataSourceTransactionManager(hikariDataSource);
    }

    @Bean(name = "hikariDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource hikariDataSource() {
        return new HikariDataSource();
    }
}