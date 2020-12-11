![xxx](https://repository-images.githubusercontent.com/168498776/44575b00-3555-11eb-8591-5f432aa8fcb2)

# 基于Spring Boot 2.X的简易脚手架项目
基于SpringBoot、通用Mapper（tkMapper）与PageHelper的深度定制版本。

## 技术选型
* String Boot 2.x
* MyBatis + tkMapper + Page Helper分页插件 + Sharding-JDBC读写分离
* MySQL + Hikari数据库连接池
* MapStruct
* Swagger2
* Sentinel限流（可选）
* Spring Cloud Admin服务监控（可选）
 
|名称|版本号|项目主页|简介|
|---|---|---|---|
|String Boot|2.2.10.RELEASE|https://spring.io/projects/spring-boot/||
|MyBatis|3.4.6|http://blog.mybatis.org||
|通用Mapper|2.1.5|https://mapperhelper.github.io||
|Page Helper|5.1.8|https://github.com/pagehelper/pagehelper-spring-boot||
|Sharding-JDBC|4.1.1|https://shardingsphere.apache.org/||
|Sentinel|1.8.0|https://github.com/alibaba/Sentinel/releases||
|Spring Boot Admin|2.2.3|https://github.com/codecentric/spring-boot-admin||

## 主要功能
除了传统SSM框架提供的特性外，还集成如下功能：
* 基于Sharding-JDBC的ID发号器
* 创建/修改时间等字段自动填充
* MySQL JSON/枚举字段自动映射
* 基于***枚举***和***数据库表***的数据字典
* 分页排序和日期参数处理
* 统一返回结果格式及异常处理
* 基于通用Mapper的MBG代码生成器、逻辑删除、乐观锁
* saveOrModify、selectMap、selectPage等

## 应用分层
|目录|说明|备注|
|---|---|---|
|app-web|Controller层+Query|MVC|
|app-biz|Service层+DTO|主要用于处理业务逻辑。|
|app-dao|DAO层+Entity|主要用于与数据库交互。|
|common|通用工具|包括工具类、枚举、各种POJO等|
|midware|中间件|包括Redis（必选）、RabbitMQ等（可选）常用中间件|
|monitor|Spring Boot Admin|监控与日志（可选）|
|sentinel|Alibaba Sentinel|限流（可选）|

## 附录：名词解释
|名词|说明|备注|
|---|---|---|
|Domain|领域模型|所有可以持久化/序列化的POJO类的泛称。|
|Entity|实体对象|属性与数据库表字段一一对应，一般由MBG生成。整个框架内均可见，但是不允许直接传递给前端。|
|DTO|数据传输对象|存在于Service方法入口、出口，或者被Controller层Resp包裹后返回给前端。|
|Query|查询对象|只能用于Controller方法入口，包裹`PageQuery`后可以作为分页查询场景查询条件传递到DAO层。|

## 致谢
* 通用Mapper和PageHelper，前人栽树，后人乘凉。
* MyBatis Plus，提供了许多值得借鉴的想法。
* jon-winters提供的[思路](https://github.com/jon-winters/auto-generate-enum-api-parent)，优雅地实现了数据字典。

--- THE END ---