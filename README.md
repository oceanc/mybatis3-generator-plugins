#Introduction

mybatis3-generator-plugins的目的是扩展[MBG](http://www.mybatis.org/generator)的代码生成功能。如其名称所示，通过mybatis3-generator-plugins生成的java代码遵循 [MyBatis 3.x](http://www.mybatis.org/mybatis-3/) 的规范。以下内容假定用户熟悉[MBG](http://www.mybatis.org/generator)的基本配置和使用。

##NOTE:
受sql dialect的限制，多数plugin目前仅支持 Mysql 5.x。

##Download
在 Maven Central 上最新的发布版本是:

```xml
<dependency>
    <groupId>com.github.oceanc</groupId>
    <artifactId>mybatis3-generator-plugin</artifactId>
    <version>0.4.0</version>
</dependency>
```

mybatis3-generator-plugins目前提供了如下可用插件：

* BatchInsertPlugin
* JacksonAnnotationPlugin
* JacksonToJsonPlugin
* LombokAnnotationPlugin
* MinMaxPlugin
* OptimisticLockAutoIncreasePlugin
* PaginationPlugin
* SliceTablePlugin
* SumSelectivePlugin
* UpdateSqlTextOfUpdateSelectivePlugin
* WhereSqlTextPlugin


#How to use it

在[MyBatis GeneratorXML Configuration File](http://www.mybatis.org/generator/configreference/xmlconfig.html)中添加你需要用到的`<plugin>`元素：

```xml
<context id="MysqlTables" targetRuntime="MyBatis3">
    <plugin type="org.mybatis.generator.plugins.SerializablePlugin"/>
    <plugin type="com.github.oceanc.mybatis3.generator.plugin.SliceTablePlugin"/>
    <plugin type="com.github.oceanc.mybatis3.generator.plugin.WhereSqlTextPlugin"/>
    <plugin type="com.github.oceanc.mybatis3.generator.plugin.BatchInsertPlugin"/>
    <plugin type="com.github.oceanc.mybatis3.generator.plugin.LombokAnnotationPlugin"/>
    <plugin type="com.github.oceanc.mybatis3.generator.plugin.PaginationPlugin"/>
    ......
    ......
</context>
```

为了举例，假设我们创建一张简单的账户表，并命名为`Account`。DDL如下：

```sql
CREATE TABLE `Account` (
  `id`            bigint(16)    NOT NULL,
  `create_time`   timestamp     NOT NULL,
  `name`          varchar(64)   DEFAULT NULL,
  `age`           tinyint(3)    DEFAULT NULL,
  `version`       int(11)       NOT NULL,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
```

##NOTE:
如果使用了SliceTablePlugin，为了其产生的作用始终有效，推荐将其放置在自定义\<plugin\>声明的第一条，如上例所示。这么做的原因是，其他plugin（BatchInsertPlugin、MinMaxPlugin、和SumSelectivePlugin）会通过检测SliceTablePlugin是否应用，来适配其效果。


<br>
##BatchInsertPlugin
该plugin是为了增加批量insert的功能。特别适用于初始化数据、或迁移数据。

###Java sample

```java
AccountMapper mapper = ...
List<Account> as = new ArrayList<>();
as.add(...)
mapper.batchInsert(as);
```
如果使用了SliceTablePlugin，则需要对List中每一个Account实例设置分表因子：

```java
AccountMapper mapper = ...
List<Account> as = new ArrayList<>();
for (Account account : accounts) {
    account.setId(...);
    // or account.setCreateTime(...);
}
mapper.batchInsert(as);
```

* 通过取模分表时，必须调用`setId`并传入合适的参数
* 通过自然月分表时，必须调用`setCreateTime`并传入合适的参数


<br>
##JacksonAnnotationPlugin
该plugin是为生成的model类添加[jackson](https://github.com/FasterXML/jackson)的`@JsonProperty`、`@JsonIgnore`、 和`@JsonFormat`注解。若使用此插件，需要额外依赖`jackson 2.5 +`。

###Xml config：
在[MyBatis GeneratorXML Configuration File](http://www.mybatis.org/generator/configreference/xmlconfig.html)的`<table>`元素中添加四个\<property\>（可选的）：

```xml
<table tableName="Account_0" domainObjectName="Account">
    <property name="jacksonColumns" value="name,age"/>
    <property name="jacksonProperties" value="nickName,realAge"/>
    <property name="jacksonFormats" value="create_time@yyyy-MM-dd HH:mm:ss"/>
    <property name="jacksonIgnores" value="id,version"/>
</table>
```
* `jacksonColumns`指需要添加`@JsonProperty`注解的列，由`,`分割的列名组成。该\<property\>必须和`jacksonProperties`成对出现
* `jacksonProperties`指`@JsonProperty`注解所需的参数值，由`,`分割，这些值的数量和顺序必须和`jacksonColumns`的值一一对应。该\<property\>必须和`jacksonColumns`成对出现
* `jacksonFormats`指需要添加`@JsonFormat`注解的列，值由`,`分割的键值对组成，键值对由`@`分割，键为列名，值为`@JsonFormat`所需参数
* `jacksonIgnores`指需要添加`@JsonIgnore`注解的列，值由`,`分割的列名组成

###Java sample

```java
public class Account implements Serializable {
    @JsonIgnore
    private Long id;
    @JsonProperty("nickName")
    private String name;
    @JsonProperty("realAge")
    private Integer age;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonIgnore
    private Long version;
}
```


<br>
##JacksonToJsonPlugin
该plugin是为生成的model类添加`toJson`方法。`toJson`方法的实现依赖于[jackson](https://github.com/FasterXML/jackson)。若使用此插件，需要额外依赖`jackson 2.5 +`。

###Java sample

```java
public class Account implements Serializable {
    public String toJson() throws IOException {
        ... ...
    }
}
```


<br>
##LombokAnnotationPlugin
该plugin是为生成的model类添加[lombok](https://projectlombok.org/)注解，避免了java bean中繁琐的setter和getter。生成的代码看起来也更干净、紧凑。若使用此插件，需要额外依赖lombok。

###Java sample

```java
@Data
public class Account implements Serializable {
    private Long id;
    ... ...
}
```


<br>
##MinMaxPlugin

###Xml config：
在[MyBatis GeneratorXML Configuration File](http://www.mybatis.org/generator/configreference/xmlconfig.html)的`<table>`元素中添加两个\<property\>（可选的）：

```xml
<table tableName="Account_0" domainObjectName="Account">
    <property name="minColumns" value="id,version"/>
    <property name="maxColumns" value="id,version"/>
</table>
```
* `minColumns`是可进行min操作的列，值由`,`分割的列名组成
* `maxColumns`是可进行max操作的列，值由`,`分割的列名组成

###Java sample

```java
AccountMapper mapper = ...
AccountExample example = new AccountExample();
example.createCriteria().andAgeEqualTo(33);
long min = mapper.minIdByExample(example);
long max = mapper.maxIdByExample(example);
```
如果使用了SliceTablePlugin，别忘了对分表因子赋值：`example.partitionFactorCreateTime(...)`


<br>
##OptimisticLockAutoIncreasePlugin
在处理并发写操作时，如果数据竞争较低，通常会采用乐观锁，避免并发问题的同时获得较好的性能。实现乐观锁的一般方式是在数据中添加版本信息，就像Account表中的version列。当写操作成功时，版本信息也相应递增。该plugin就是解决version自动递增问题的，可以避免手动对version+1。

###Xml config：
在[MyBatis GeneratorXML Configuration File](http://www.mybatis.org/generator/configreference/xmlconfig.html)的`<table>`元素中添加一个\<property\>：

```xml
<table tableName="Account_0" domainObjectName="Account">
    <property name="optimisticLockColumn" value="version"/>
</table>
```
* `optimisticLockColumn`指具有版本信息语义的列，`version`是具体的列名

###Java sample

```java
AccountMapper mapper = ...
Account record = new Account();
record.setName("tom");
// record.setVersion(1) 无须手工对version进行赋值
AccountExample example = new AccountExample();
example.createCriteria().andAgeEqualTo(33);
mapper.updateByExampleSelective(record, example);
```
如果使用了SliceTablePlugin，别忘了对分表因子赋值：`example.partitionFactorId(...)`


<br>
##PaginationPlugin

###Java sample

```java
AccountMapper mapper = ...
AccountExample example = new AccountExample();
example.page(0, 2).createCriteria().andAgeEqualTo(33);
List<Account> as = mapper.selectByExample(example);
```
如果使用了SliceTablePlugin，别忘了对分表因子赋值：`example.partitionFactorCreateTime(...)`


<br>
## SliceTablePlugin

当数据库单表数据量过大时，可以通过水平拆分把单表分解为多表。例如，若Account表中的预期数据量过大时（也许5、6亿），可以把一张Account表分解为多张Account表。**SliceTablePlugin并不会自动创建和执行拆分后的DDL，因此必须手工创建DDL并按下述约定修改表名。**

SliceTablePlugin目前支持两种分表命名方式：

* 对指定列取模。拆分后的表名为`原表名_N`。如：Account\_0、Account\_1、Account\_3 ......
* 对指定的时间类型列，按单自然月拆分。拆分后的表名为`原表名_yyyyMM`。如：Account\_201501、Account\_201502 ...... Account\_201512


###Xml config：
####1.利用取模  
假如通过Account表的`id`字段做分表，计划拆分为`97`张表。在[MyBatis GeneratorXML Configuration File](http://www.mybatis.org/generator/configreference/xmlconfig.html)的`<table>`元素中添加两个\<property\>：

```xml
<table tableName="Account_0" domainObjectName="Account">
    <property name="sliceMod" value="97"/>
    <property name="sliceColumn" value="id"/>
</table>
```
* `sliceMod`指按取模方式分表，`97`是取模的模数
* `sliceColumn`指取模所使用的列，`id`是具体列名

####2.利用自然月  
假如通过Account表的`create_time `字段做拆分:

```xml
<table tableName="Account_0" domainObjectName="Account">
    <property name="sliceMonth" value="1"/>
    <property name="sliceColumn" value="create_time"/>
</table>
```
* `sliceMonth `指按自然月分表，`1`指按单个自然月。**Note：目前只支持按单月分表，此处的值 1 无实际意义**
* `sliceColumn`指时间类型的列，`create_time`是具体列名


###Java sample
####insert

```java
AccountMapper mapper = ...
Account record = new Account();
record.setAge(33);
record.setId(101);
record.setCreateTime(new Date());
mapper.insert(record);
// or mapper.insertSelective(record)
```
* 通过取模分表时，必须调用`setId`并传入合适的参数
* 通过自然月分表时，必须调用`setCreateTime`并传入合适的参数

####read

```java
AccountMapper mapper = ...
AccountExample example = new AccountExample();
example.partitionFactorId(id).createCriteria().andAgeEqualTo(33);
// or example.partitionFactorCreateTime(new Date()).createCriteria().andAgeEqualTo(33);
List<Account> as = mapper.selectByExample(example);
```
* 通过取模分表时，`partitionFactorId`方法表示分表因子是Id字段，必须调用该方法并传入合适的参数
* 通过自然月分表时，`partitionFactorCreateTime`方法表示分表因子是createTime字段，必须调用该方法并传入合适的参数

####update

```java
AccountMapper mapper = ...
Account record = new Account();
record.setAge(33);
AccountExample example = new AccountExample();
example.partitionFactorId(id).createCriteria().andAgeEqualTo(33);
// or example.partitionFactorCreateTime(new Date()).createCriteria().andAgeEqualTo(33);
mapper.updateByExampleSelective(record, example);
```
上例的用法和read操作一样，在example对象上必须调用`partitionFactorId`或`partitionFactorCreateTime`方法。  
除此之外，还可以用如下方式进行update：

```java
AccountMapper mapper = ...
Account record = new Account();
record.setCreateTime(new Date());
record.setId(101);
// or record.setCreateTime(new Date());
AccountExample example = new AccountExample();
example.createCriteria().andAgeEqualTo(33);
mapper.updateByExampleSelective(record, example);
```
由于在record对象调用了`setId`或`setCreateTime`，就无须在example对象指定分表因子。

####delete

```java
AccountMapper mapper = ...
AccountExample example = new AccountExample();
example.partitionFactorId(id).createCriteria().andAgeEqualTo(33);
// or example.partitionFactorCreateTime(new Date()).createCriteria().andVersionEqualTo(0);
mapper.deleteByExample(example);
```
* 通过取模分表时，`partitionFactorId`方法表示分表因子是Id字段，必须调用该方法并传入合适的参数
* 通过自然月分表时，`partitionFactorCreateTime`方法表示分表因子是createTime字段，必须调用该方法并传入合适的参数

####other
当无法获得分表因子的值时、或者确定所操作的表名时，可以通过:

* `record.setTableNameSuffix(...)`取代`record.setId(...)`或`record.setId(...)`
* `example.setTableNameSuffix(...)`取代`example.partitionFactorId(...)`

<tt style="background-color:#F9FF00;"> WARNING</tt>：由于`setTableNameSuffix`的参数是String类型，在 Mybatis3 的 mapper xml 中生成`${}`变量，这种变量不会做sql转义，而直接嵌入到sql语句中。如果以用户输入作为`setTableNameSuffix`的参数，会导致潜在的`SQL Injection`攻击，需谨慎使用。


<br>
##SumSelectivePlugin

###Java sample

```java
AccountMapper mapper = ...
AccountExample example = new AccountExample();
example.sumAge().createCriteria().andVersionEqualTo(0);
long sum = mapper.sumByExample(example);
```
如果使用了SliceTablePlugin，别忘了对分表因子赋值：`example.partitionFactorCreateTime(...)`


<br>
##UpdateSqlTextOfUpdateSelectivePlugin

###Java sample

```java
AccountMapper mapper = ...
Account record = new Account();
record.setUpdateSql("version = version + 1")
record.setAge(33);
AccountExample example = new AccountExample();
example.createCriteria().andAgeEqualTo(22);
mapper.updateByExampleSelective(record.setAge, example);
```
如果使用了SliceTablePlugin，别忘了对分表因子赋值：`example.partitionFactorCreateTime(...)`  
<tt style="background-color:#F9FF00;"> WARNING</tt>：由于`setUpdateSql`的参数是String类型，在 Mybatis3 的 mapper xml 中生成`${}`变量，这种变量不会做sql转义，而直接嵌入到sql语句中。如果以用户输入作为`setUpdateSql`的参数，会导致潜在的`SQL Injection`攻击，需谨慎使用。


<br>
##WhereSqlTextPlugin

###Java sample

```java
AccountMapper mapper = ...
int v = 1;
AccountExample example = new AccountExample();
example.createCriteria().andAgeEqualTo(33).addConditionSql("version =" + v + " + 1");
List<Account> as = mapper.selectByExample(example);
```
如果使用了SliceTablePlugin，别忘了对分表因子赋值：`example.partitionFactorCreateTime(...)`  
<tt style="background-color:#F9FF00;"> WARNING</tt>：由于`addConditionSql`的参数是String类型，在 Mybatis3 的 mapper xml 中生成`${}`变量，这种变量不会做sql转义，而直接嵌入到sql语句中。如果以用户输入作为`addConditionSql`的参数，会导致潜在的`SQL Injection`攻击，需谨慎使用。