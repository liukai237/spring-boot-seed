package com.yodinfo.seed.config;

import com.yodinfo.seed.support.AutoColumnFillInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
//@MapperScan(basePackages = "com.yodinfo.seed.dao")
@EnableTransactionManagement
public class DbConfig {

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    /**
     * 自动创建create_time和update_time
     */
    @PostConstruct
    public void addPageInterceptor() {
        AutoColumnFillInterceptor interceptor = new AutoColumnFillInterceptor();
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
        }
    }
}