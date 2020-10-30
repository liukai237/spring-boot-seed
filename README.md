# 基于Spring Boot 2.X的简易脚手架项目
此版本为通用Mapper与PageHelper深度定制版本。

## 技术选型
* String Boot 2.x
* MyBatis + tkMapper + Page Helper分页插件 + Sharding-JDBC读写分离
* MySQL + Hikari数据库连接池
* MapStruct
* Swagger2
 
|名称|版本号|项目主页|简介|
|---|---|---|---|
|String Boot|2.1.11.RELEASE|https://spring.io/projects/spring-boot/||
|MyBatis|3.4.6|http://blog.mybatis.org||
|通用Mapper|2.1.5|https://mapperhelper.github.io||
|Page Helper|5.1.8|https://github.com/pagehelper/pagehelper-spring-boot||
|Sharding-JDBC|4.1.1|https://shardingsphere.apache.org/||

# 《开发约定》
## 0x00 通用约定
* 中心思想：约定重于配置；追求小而美，避免过度封装。
* 编程风格基本遵守《阿里巴巴Java编程规约》，部分微调，比如数据库领域模型不需要加DO。

## 0x01 Controller层开发约定
Controller层应该越“薄”越好，主要用于DTO/Domain转换，提供详细的Swagger文档，并使用MockMvc进行单元测试。

### 1. 统一封装的响应结果
* 返回Http Status统一为200。
* 返回结果统一封装成code、msg、data三段式结构。并额外提供冗余字段success。
* Controller层继承BaseController，使用ok()、fail()方法返回结果。
> 之所以不提供类似R.ok()的方法是为了防止滥用封装对象。`Resp`对象只应该出现在Controller层。
* 所有的Controller都应该提供Swagger文档，尽量不要返回Resp<?>或者Map。

### 2. 参数校验
* 注意区分***基础数据校验***和***业务数据校验***，比如“手机号码不能为空”属于前者，“手机号码尚未注册”属于后者。
* 基础数据校验使用Hibernate Validator，通过JSR303注解进行校验。如果预设的错误码无法满足要求，可通过`@ErrorCode`注解重新定义错误码。
* 业务数据校验一般通过代码逻辑进行判断，Controller层可通过fail()方法返回，Service层通过抛出`BusinessException`实现。

### 3. 日期处理
全局统一配置日期格式如下：

|对象|Date|LocalDateTime|LocalDate|LocalTime|
|---|---|---|---|---|
|格式|yyyy-MM-dd HH:mm:ss|yyyy-MM-dd HH:mm:ss|yyyy-MM-dd|HH:mm:ss|

#### 例1：
```java
@GetMapping(value = "/hello")
public Date getTimestamp() {
    return new Date();
}
```

Response ：2020-02-21 12:15:45

#### 例2：
```java
@GetMapping(value = "/hello/{someDate}")
public LocalDateTime getTimestamp(@PathVariable LocalDateTime someDate) {
    return someDate;
}
```
Request：
```jshelllanguage
curl http://localhost:8080/hello/2020-02-21+12%3a15%3a45
```
Response：2020-02-21 12:15:45

#### 例3：
```java
@GetMapping(value = "/hello/")
public LocalDate getTimestamp(@RequestParam LocalDate someDate) {
    return someDate;
}
```
Request：
```jshelllanguage
curl http://localhost:8080/helloTime/?someDate=2020-02-21
```
Response ：2020-02-21

#### 例4：
```java
@GetMapping(value = "/hello/")
public Date getTimestamp(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam Date someDate) {
    return someDate;
}
```
Request：
```jshelllanguage
curl http://localhost:8080/helloTime/?someDate=2020-02-21
```
Response ：2020-02-21 00:00:00

#### 例5：
```java
@Data
public class Student {
    private Long id;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    
    private LocalDateTime createTime;
}
```
序列化后：
```json
{
    "id": "9223372036804940651",
    "birthday": "2009-03-08",
    "createTime": "2019-07-10 13:40:06"
}
```
注意：JSON序列化时，@DateTimeFormat注解换成了@JsonFormat。
> BTW，id字段是Long类型，可能存在精度丢失，因此全局转为字符串。

### 4. 开始日期和结束日期
时间范围参数可以还可以使用自定义注释@StartDate和@EndDate，前者精确到00:00:00:000，后者精确到23:59:59:999。  
比如：
```java
@ApiModel(value = "SomeQueryParam", description = "查询参数")
public class SomeQueryParam {
    @StartDate
    @ApiModelProperty(name = "startDate", value = "开始日期，自动补齐00:00:00", example = "2019-02-21")
    private Date startDate;
    
    @EndDate
    @ApiModelProperty(name = "endDate", value = "结束日期，自动补齐23:59:59", example = "2019-02-21")
    private Date endDate;
}
```
> 当天示例：`{"startDate": "2018-11-07","endDate": "2018-11-07"}`，即表示：2018年11月7日零点到2018年11月7日23点59分59秒之间的时间段。

再比如：
```java
  public void initData(@StartDate @RequestParam Date since, @EndDate @RequestParam Date until);
```

### 5. 排序
* RESTful风格参数，支持a+b-c+和+a,-b,+c两种格式
> e.g.
> ```jshelllanguage
>  curl http://localhost:8080/users?sort=+createTime,-id
> ```
* Request Body方式，传入数组，支持多重排序
> ```json 
> {
>   “sorting”: [
>     {
>       "field": "createTime",
>       "order": "desc"
>     }
>   ]
> }
> ```
> 注意：对于数据库表不存在的字段，需要在Service层，或者SQL语句中单独处理。

### 6. 分页工具
* 分页参数`pageNum`和`pageSize`，pageSize默认为`0`，即不分页。
* 使用PageHelper作为默认分页插件，自定义的`Paged`作为分页结果容器。
* 关于Converter转换器，除分页场景，应该只在Controller层使用。

e.g.
```java
public Paged<UserDto> findUserWithPage(Integer pageNum, Integer pageSize) {
    PageHelper.startPage(pageNum, pageSize, "update_time desc");
    return new Paged<>(userMapper.selectAll(), UserConverter.INSTANCE::toDto);
}
```
> 除此之外还提供了一个`@StartPage`注解，简单替代`PageHelper.startPage()`，不再需要重复输入参数。
> ```java
>   @StartPage(pageNum = 1, pageSize = 10)
>   public List<String> autoComplate(String keyword) {
>     return fooMapper.selectByKeyword(keyword));
>   }
> 
>   @StartPage(orderBy = "create_time desc")
>   public Paged<UserInfo> findWithPage(Integer pageNum, Integer pageSize) {
>     return new Paged<>(userInfoMapper.selectAll());
>   }
> ```
> 注意：注解中的属性是默认值，优先级低于参数列表中的值。
>

### 7. 枚举处理
* 枚举类一般继承`CodeEnum`，前端看到的是`code`和`desc`描述字段，其中code是小写的枚举name，而不是数据库中真实的code字段；
* 接口调用时，使用`code`（不区分大小写）即可反序列化成对应枚举。

### 8. 其他
* 当参数名为复数时，值为数组。参数值为null或者不传表示***全部***，不要使用空字符串或者`ALL`。

## 0x02 业务层开发约定
主要业务逻辑都应该写在Service层，除分页方法外，应该尽量做到可复用。对于其它领域对象的引用，优先导入Service而不是Mapper。
* 只读方法增加注解`@Transactional(readOnly = true)`，修改方法必须增加注释`@Transactional(rollbackFor = Exception.class)`。
* 在Service层而不是Controller层打印业务日志。
* 在Service层而不是DAO层实现saveOrModify。
* 不要在循环中进行IO操作，比如文件和数据库读写。

## 0x03 DAO层开发约定
### 主键生成策略
提供DefaultGenId和UuidGenId两种主键生成策略。前者是可排序的数字，后者是UUID。
比如，Entity ID字段增加注解`@KeySql(genId = DefaultGenId.class)`即可实现全局唯一ID。

e.g.
```java
@Data
@Table(name = "t_user")
public class User extends BaseDomain {

    @Id
    @KeySql(genId = DefaultGenId.class)
    private Long userId;
    private String username;
}
```

### 枚举、对象映射
* 枚举类型实现CodeEnum，即可自动完成Integer与Enum对象之间的映射。
> 可参考`Gender.java`实现。注意：API接口传递的code是枚举的name（不区分大小写），而不是数据库中真实的code字段。

* JSON字段对应的Java对象需要手动创建，并继承实现`AbstractJsonTypeHandler`，例如：
```java
class Foo {
    // ...
}

class FooJsonHandler<Foo> extends AbstractJsonTypeHandler {
    // ...
}
```
然后做如下配置：
```java
@ColumnType(typeHandler = FooJsonHandler.class)
private Foo foo;
```
> 注意：如果存在XML Mapper，需要同步修改。

### 自动填充字段
使用`@CreatedDate`和`@LastModifiedDate`注解的字段，如果没有赋值，将会自动被填充系统时间。

## 0x04 其他开发约定

### 异常处理
* Controller使用统一封装的ok()、fail()方法返回，而不是抛出异常。
* 所有Checked Exception使用`BusinessException`重新封装，统一处理。
> 比如Service层返回错误消息：“用户名重复!”。

### 代码生成器
提供基于tkMapper的代码生成器（MBG），只负责生成实体类、DAO和XML Mapper，并且实体类没有配置ID生成策略，需要自行配置。

### Code Review
* 代码评审前应该先进行静态代码检查，不要把时间和精力浪费在低级缺陷上。
* 原则上所有Controller层暴露的API接口和MyBatis XML中的SQL语句都应该进行评审。

--- END ---