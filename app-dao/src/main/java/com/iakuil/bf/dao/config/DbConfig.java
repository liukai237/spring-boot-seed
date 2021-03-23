package com.iakuil.bf.dao.config;

import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
import com.dangdang.ddframe.rdb.sharding.id.generator.self.CommonSelfIdGenerator;
import com.iakuil.bf.common.db.EntityAuditingInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 数据库配置文件
 *
 * @author Kai
 */
@Configuration
@MapperScan(basePackages = "com.iakuil.bf.dao")
@EnableTransactionManagement
public class DbConfig {

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    /**
     * 自定义的拦截器
     */
    @PostConstruct
    public void addPageInterceptor() {
        // 自动填充字段
        EntityAuditingInterceptor auditor = new EntityAuditingInterceptor();

        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            configuration.addInterceptor(auditor);
        }
    }

    /**
     * Sharding-JDBC ID发号器
     */
    @Bean
    public IdGenerator getIdGenerator() {
        return new CommonSelfIdGenerator();
    }
}