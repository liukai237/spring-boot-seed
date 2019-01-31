## 技术选型
* String Boot 2.x
* MyBatis
* Page Helper分页插件
* Druid数据库连接池
* 通用Mapper
* Easy Mapper https://github.com/neoremind/easy-mapper

## 基本需求：
一个用户可拥有多个角色，一个角色可授予多个用户
不可对用户直接授权
角色有优先级的概念，即当某个用色具有多个用色时，按优先级高低来判断权限
实现模块级控制
  
|名称|版本号|项目主页|简介|
|---|---|---|---|
|String Boot||||
|MyBatis||||
|Page Helper||https://github.com/pagehelper/pagehelper-spring-boot||
|Druid||https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter||
|通用Mapper||https://mapperhelper.github.io||
|Orika|1.5.2|http://orika-mapper.github.io/orika-docs/|simpler, lighter and faster Java bean mapping|

详细(文档)[http://localhost:8888/swagger-ui.html]