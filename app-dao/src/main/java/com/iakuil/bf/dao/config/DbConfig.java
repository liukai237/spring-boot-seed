package com.iakuil.bf.dao.config;

import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
import com.dangdang.ddframe.rdb.sharding.id.generator.self.CommonSelfIdGenerator;
import com.iakuil.bf.common.db.AutoColumnFillInterceptor;
import com.iakuil.bf.common.db.LogicallyDeletedInterceptor;
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

/**
 * 数据库配置文件
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
        // 乐观锁
        OptimisticLockerInterceptor locker = new OptimisticLockerInterceptor();
        Properties props = new Properties();
        props.setProperty("versionColumn", "version");
        locker.setProperties(props);

        // 逻辑删除
        LogicallyDeletedInterceptor deleted = new LogicallyDeletedInterceptor();
        Properties props2 = new Properties();
        props2.setProperty("logicDeleteField", "deleted");
        props2.setProperty("logicDeleteValue", "1");
        props2.setProperty("logicNotDeleteValue", "0");
        props2.setProperty("dbType", "mysql");
        deleted.setProperties(props2);

        // 自动填充字段
        AutoColumnFillInterceptor autoFill = new AutoColumnFillInterceptor();

        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            configuration.addInterceptor(locker);
            configuration.addInterceptor(deleted);
            configuration.addInterceptor(autoFill);
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