## 技术选型
* String Boot 2.x
* MyBatis + 通用Mapper + Page Helper分页插件
* MySQL/PostgreSQL + Hikari数据库连接池
* MapStruct
* Swagger2
* Shiro

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
|String Boot|2.1.11.RELEASE|https://spring.io/projects/spring-boot/||
|MyBatis|3.4.6|http://blog.mybatis.org||
|通用Mapper|2.1.5|https://mapperhelper.github.io||
|Page Helper|5.1.8|https://github.com/pagehelper/pagehelper-spring-boot||

## 附件
详细(文档)[http://localhost:8089/doc.html]

> PS. 使用PageHelp而不是通用Mapper来排序

### 接口约定
#### 通用约定
* 所有参数使用驼峰风格。
* 当参数名为复数时，值为数组。参数值为null表示***全部***，不要使用空字符串或者`ALL`。
* 分页参数`pageNum`和`pageSize`，pageSize默认为`0`，即不分页。

#### 简单数据展示（分页、排序及简单过滤条件）
* 使用GET请求。
* 排序参数`sort`放在query，支持`a+b-c+`和`+a,-b,+c`两种格式。
* 默认日期查询参数：`startDate`和`endDate`，格式为yyyy-MM-dd。当天示例：`?startDate=2018-11-07&endDate=2018-11-07`
* 其他参数放在path或者query，尽量不要超过三个，否则应使用POST封装。

#### 复杂过滤条件数据展示
* 使用POST请求。
* 所有参数封装在body。
* 默认日期查询参数：`startDate`和`endDate`，格式为yyyy-MM-dd。当天示例：`{"startDate": "2018-11-07","endDate": "2018-11-07"}`

#### 返回值
* 返回Http Status统一为200。
* 返回结果统一封装成code、msg、data三段式结构。
* 除token接口，全部使用驼峰命名。

### 编程风格
#### 命名规范
##### Dao 接口命名
* insert
* batchInsert
* selectOne
* selectById
* count
* selectList
* update
* deleteById

##### Service 接口命名
* add
* findById
* findByXXX
* findXXXList
* modify
* remove

#### 业务层
* Service方法入参不要超过3个，多个参数使用Map<String, Object>。
* 除分页方法外，返回值不应该直接使用DTO，domain转DTO应在Controller层完成。
* 只读方法增加注解`@Transactional(readOnly = true)`，修改方法必须增加注释`@Transactional(rollbackFor = Exception.class)`。

#### 异常处理
* Controller使用统一封装的ok()、fail()方法返回。
* Service层使用`IllegalStatusException`返回错误码和消息。
* 所有Checked Exception使用`BusinessException`重新封装，统一处理。

