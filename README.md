## 技术选型
* String Boot 2.x
* MyBatis + 通用Mapper + Page Helper分页插件
* PostgreSQL + Hikari数据库连接池
* MapStruct
* Swagger2

## 基本需求：

### CRUD模块
* 用户输入校验
* 实现基本的CRUD操作

### 文档模块
待补充……

### 配置模块
待补充……

### 熔断与重试模块
待补充……

### 权限模块（待实现）
* 一个用户可拥有多个角色，一个角色可授予多个用户
* 不可对用户直接授权
* 角色有优先级的概念，即当某个用色具有多个用色时，按优先级高低来判断权限
* 实现模块级控制
  
|名称|版本号|项目主页|简介|
|---|---|---|---|
|String Boot|2.0.5.RELEASE|https://spring.io/projects/spring-boot/||
|MyBatis|3.4.6|http://blog.mybatis.org||
|通用Mapper|2.1.4|https://mapperhelper.github.io||
|Page Helper|5.1.8|https://github.com/pagehelper/pagehelper-spring-boot||

## 附件
详细(文档)[http://localhost:8888/swagger-ui.html]