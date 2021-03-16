![logo](https://repository-images.githubusercontent.com/168498776/44575b00-3555-11eb-8591-5f432aa8fcb2)

# 基于Spring Boot 2.X的简易脚手架项目
基于SpringBoot、通用Mapper（tkMapper）与PageHelper的深度定制版本。

## 技术选型
* String Boot 2.x
* MyBatis + tkMapper + Page Helper分页插件 + Sharding-JDBC读写分离
* MySQL + Hikari数据库连接池
* MapStruct
* Swagger2
* Apache Shiro
 
|名称|版本号|项目主页|简介|
|---|---|---|---|
|String Boot|2.2.10.RELEASE|https://spring.io/projects/spring-boot/||
|MyBatis|3.4.6|http://blog.mybatis.org||
|通用Mapper|2.1.5|https://mapperhelper.github.io||
|Page Helper|5.1.8|https://github.com/pagehelper/pagehelper-spring-boot||
|Sharding-JDBC|4.1.1|https://shardingsphere.apache.org/||
|Shiro|1.7.0|http://shiro.apache.org/||

## 主要功能
除了传统SSM框架提供的特性外，还集成如下功能：
* 通用Mapper和***通用Service***
* 统一返回结果格式及异常处理
* 分页排序和日期参数处理
* 基于Sharding-JDBC的ID发号器
* 创建/修改时间等字段自动填充
* MySQL JSON/枚举字段自动映射
* 基于***枚举***和***数据库表***的数据字典
* 基于通用Mapper的MBG代码生成器
* 基于Caffeine和Redis的两级缓存
* 逻辑删除、乐观锁、批量插入等

## 应用分层
|目录|说明|备注|
|---|---|---|
|app-web|Controller层+Query|提供接口、基础数据校验以及VO封装|
|app-biz|Service层+DTO|处理业务逻辑，DTO封装（主要指outDTO，inDTO一般使用Entity或者Example）|
|app-dao|DAO层+Entity|ORM框架，Entity封装。|
|common|通用工具|包括工具类、枚举、各种常量等|
|midware|中间件|包括Redis（必选）、RabbitMQ（可选）、Quartz（可选）等常用中间件|
|monitor|Spring Boot Admin|监控与日志（可选）|

<sub>Copyright (c) 2020 iakuil.com</sub>