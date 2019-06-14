package com.yodinfo.seed.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@MapperScan(basePackages = "com.yodinfo.seed.dao")
@EnableTransactionManagement
public class DbConfig {
}