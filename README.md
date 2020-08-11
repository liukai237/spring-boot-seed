# 基于Spring Boot 2.X的简易脚手架项目
Swagger(文档)[http://localhost:8089/doc.html]

## 技术选型
* String Boot 2.x
* MyBatis + tkMapper + Page Helper分页插件
* MySQL + Hikari数据库连接池
* MapStruct
* Swagger2
 
|名称|版本号|项目主页|简介|
|---|---|---|---|
|String Boot|2.1.11.RELEASE|https://spring.io/projects/spring-boot/||
|MyBatis|3.4.6|http://blog.mybatis.org||
|通用Mapper|2.1.5|https://mapperhelper.github.io||
|Page Helper|5.1.8|https://github.com/pagehelper/pagehelper-spring-boot||

# 《开发约定》
## 0x00 通用约定
* 中心思想：约定重于配置；追求小而美，避免过度封装.
* 编程风格基本遵守《阿里巴巴Java编程规约》，部分微调，比如数据库领域模型不需要加DO。

## 0x01 Controller层开发约定

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
    @JsonSerialize(using = ToStringSerializer.class)
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
需要注意的是：JSON序列化时，@DateTimeForma注解换成了@JsonFormat。
> BTW，id字段可能会有精度丢失，因此需要加上注解`@JsonSerialize(using = ToStringSerializer.class)`。

### 4. 开始日期和结束日期
时间范围参数在后台入参处增加注释@StartDate和@EndDate，前者精确到00:00:00:000，后者精确到23:59:59:999。
```java
@ApiModel(value = "BondQueryParam", description = "债券查询参数")
public class BondQueryParam {
    @StartDate
    @ApiModelProperty(name = "startDate", value = "开始日期，自动补齐00:00:00", example = "2019-02-21")
    private Date startDate;
    
    @EndDate
    @ApiModelProperty(name = "endDate", value = "结束日期，自动补齐23:59:59", example = "2019-02-21")
    private Date endDate;
}
```
> 当天示例：`{"startDate": "2018-11-07","endDate": "2018-11-07"}`，即表示：2018年11月7日零点到2018年11月7日23点59分59秒之间的时间段。

### 5. 枚举处理
枚举类一般继承`LabelEnum`，实现数字和枚举对象的映射。URL参数和JSON字段均支持，建议作为JSON参数使用。

### 6. 其他
* 当参数名为复数时，值为数组。参数值为null表示***全部***，不要使用空字符串或者`ALL`。
* 分页参数`pageNum`和`pageSize`，pageSize默认为`0`，即不分页。
* 排序支持a+b-c+和+a,-b,+c两种格式，以及URL参数和JSON字段两种位置。
> e.g.
> ```jshelllanguage
>  curl http://localhost:8080/users?sort=+create_time,-id
> ```
> ```java 
> {
>   sort: create_time+id-
> }
> ```

## 0x02 业务层开发约定
* 只读方法增加注解`@Transactional(readOnly = true)`，修改方法必须增加注释`@Transactional(rollbackFor = Exception.class)`。

### 异常处理
* Controller使用统一封装的ok()、fail()方法返回。
* 所有Checked Exception使用`BusinessException`重新封装，统一处理。比如Service层返回错误消息：“用户名重复!”。

## 0x03 DAO层开发约定
### 主键生成策略
### 枚举、对象映射
### 自动填充字段
### 分页工具
### 代码生成器

## 0x04 其他开发约定

