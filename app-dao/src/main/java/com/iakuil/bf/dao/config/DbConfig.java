package com.iakuil.bf.dao.config;

import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
import com.dangdang.ddframe.rdb.sharding.id.generator.self.CommonSelfIdGenerator;
import com.iakuil.bf.common.db.AutoColumnFillInterceptor;
import com.iakuil.bf.common.db.OptimisticLockerInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;

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
        AutoColumnFillInterceptor autoFill = new AutoColumnFillInterceptor();

        OptimisticLockerInterceptor locker = new OptimisticLockerInterceptor();
        Properties props = new Properties();
        props.setProperty("versionColumn", "version");
        locker.setProperties(props);

        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            configuration.addInterceptor(autoFill);
            configuration.addInterceptor(locker);
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