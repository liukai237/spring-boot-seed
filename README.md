## 技术选型
* String Boot 2.x
* MyBatis + 通用Mapper + Page Helper分页插件
* PostgreSQL + Hikari数据库连接池
* MapStruct
* Swagger2
* Shiro(备选)

## 基本需求：
* 统一的权限管理
* 统一的接口校验
* 统一的异常处理
* 统一的分页排序

### CRUD模块
* JSV实现接口参数校验
* 实现基本的CRUD操作
* 实现复杂的查询过滤及分页排序

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

> PS. 使用PageHelp而不是通用Mapper来排序

### 接口约定
* 分页参数`pageNum`和`pageSize`放在query，pageSize默认为`0`，即不分页。
* 排序参数`sort`也放在query，支持`a+b-c+`和`+a,-b,+c`两种格式。
* 其他查询参数等放在body。当参数名为复数时，值为数组。参数值为null表示`全部`，不要使用~~~空字符串~~~或者~~~ALL~~~。
* 日期查询参数：`startDate`和`endDate`，格式为yyyy-MM-dd。当天示例：`{"startDate": "2018-11-07","endDate": "2018-11-07"}`
