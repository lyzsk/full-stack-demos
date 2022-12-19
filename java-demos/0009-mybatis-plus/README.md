# mybatis-plus-i

-   创建 `mybatis_plus` 数据库, 执行 sql

    ```sql
    CREATE TABLE mybatis_plus.`user` (
    	`id` BIGINT(20) NOT NULL COMMENT '主键ID',
    	`name` VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    	`age` INT(11) NULL DEFAULT NULL COMMENT '年龄',
    	`email` VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    	PRIMARY KEY (id)
    )
    ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_0900_ai_ci;

    INSERT INTO USER (id, name, age, email) VALUES
    (1, 'Jone', 18, 'test1@baomidou.com'),
    (2, 'Jack', 20, 'test2@baomidou.com'),
    (3, 'Tom', 28, 'test3@baomidou.com'),
    (4, 'Sandy', 21, 'test4@baomidou.com'),
    (5, 'Billie', 24, 'test5@baomidou.com');
    ```

-   添加依赖

    ```xml
    		<dependency>
    			<groupId>org.projectlombok</groupId>
    			<artifactId>lombok</artifactId>
    			<scope>runtime</scope>
    		</dependency>
    		<dependency>
    			<groupId>mysql</groupId>
    			<artifactId>mysql-connector-java</artifactId>
    			<scope>runtime</scope>
    		</dependency>
    		<dependency>
    			<groupId>com.baomidou</groupId>
    			<artifactId>mybatis-plus-boot-starter</artifactId>
    			<version>3.5.1</version>
    		</dependency>
    ```

-   创建实体类

    ```java
    @Data
    public class User {
    	private Long id;

    	private String name;

    	private Integer age;

    	private String email;
    }
    ```

-   创建实体类对应 mapper

    因为 `MapperScan` 扫描的是接口, 而在 IOC 容器中只能存在类所对应的 Bean 不能存在接口所对应的 Bean, 所以 idea 接口动态生成的代理类无法进行自动装配, 所以要添加 `@Mapper` 或者 `@Repository`

    ```java
    @Mapper
    public interface UserMapper extends BaseMapper<User> {
    }
    ```

-   启动类添加 `@MapperScan`

    ```java
    @SpringBootApplication
    @MapperScan("cn/sichu/mybatis/plus/mapper")
    public class MybatisPlusIApplication {

    	public static void main(String[] args) {
    		SpringApplication.run(MybatisPlusIApplication.class, args);
    	}

    }
    ```

-   为了添加 sql 语句和方法的日志, 修改 `application.yml`

    ```yml
    mybatis-plus:
    	configuration:
    		log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    ```

## custom query mapper

> 注意, mybatis-plus 默认的 `mapper-locations:` 是 `resource/mapper/**/*.xml`, 如果不在 yml 里自定义的话就要按默认的来创建目录

自定义查询:

1. 根据 id 查询用户, 返回 map 集合:

```java
    /**
     * 根据id查询用户信息为Map集合
     *
     * @param id
     * @return
     */
	@MapKey("id")
    Map<String, Object> selectMapById(Long id);
```

```xml
    <select id="selectMapById" resultType="java.util.Map">
        select id,name,age,email from user where id=#{id}
    </select>
```

## custom service implementation

通用 Service CRUD 封装 IService (opens new window)接口，进一步封装 CRUD 采用 get 查询单行 remove 删除 list 查询集合 page 分页 前缀命名方式区分 Mapper 层避免混淆

```java
public interface UserService extends IService<User> {
}
```

因为 UserService 已经有实现类 `ServiceImpl<M extends BaseMapper<T>, T> implements IService<T>` 了, 所以自己实现接口的时候应该是先继承再实现

```java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
```

## change tablename

如果实体类创建后, 想要修改 sql 中的 tablename

1. 可以使用 `@TableName("table_name")` 注解:

    ```java
    @Data
    @TableName("t_user")
    public class User {}
    ```

2. 可以更改 yml 里的全局配置, 设置实体类所对应的表名的统一前缀:

    ```yml
    mybatis-plus:
    	global-config:
    		db-config:
    			table-prefix: t_
    ```

## change table key column

如果将 sql 表中的 id 改成 uid, 并且更改实体类中的属性为 `private Long uid;` 就会有问题

因为 MyBatisPlus 的 id 是用雪花算法(snowflake)来生成 id 的, 换成 uid 肯定不好使(否则换任何 xxid 都可以了...)

比如测试 insert, 会报错:

```
### SQL: INSERT INTO t_user  ( name, age, email )  VALUES  ( ?, ?, ? )
### Cause: java.sql.SQLException: Field 'uid' doesn't have a default value
```

这时候可以使用 `@TableId` 将某一属性的字段指定为 id:

```java
    @TableId
    private Long uid;
```

此时日志里执行的 sql 信息就有 id 为 uid 字段了:

```
==>  Preparing: INSERT INTO t_user ( uid, name, age, email ) VALUES ( ?, ?, ?, ? )
```

此时如果不修改数据库里的 uid 列名, 只修改实体类里的属性为 id:

```java
    @TableId
    private Long id;
```

这样会报错:

```
### SQL: INSERT INTO t_user  ( id, name, age, email )  VALUES  ( ?, ?, ?, ? )
### Cause: java.sql.SQLSyntaxErrorException: Unknown column 'id' in 'field list'
```

这时候要添加注解里的 value 值:

```java
    @TableId(value = "uid")
    private Long id;
```

这样就可以正确指定主键的字段(如果只用一个属性也可以写成`@TableId("uid")`)

```
==>  Preparing: INSERT INTO t_user ( uid, name, age, email ) VALUES ( ?, ?, ?, ? )
```

## idtype

可以设置`@TableId(value = "prim_key_name", type = "")` 来设置 `IdType` 里的生成策略, 默认使用雪花算法(snowflake)

如果想要是自增主键:

方法一:

1. 修改数据库主键为自增

2. 设置 `type = IdType.AUTO`:

    ```java
    @TableId(value = "uid", type = IdType.AUTO)
    private Long id;
    ```

    此时就能得到自增的添加

    ```
    ==>  Preparing: INSERT INTO t_user ( name, age, email ) VALUES ( ?, ?, ? )
    ==> Parameters: uidtest(String), 18(Integer), uidtest@test.com(String)
    <==    Updates: 1
    Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@40230eb9]
    res:1
    id:6
    ```

当然, 如果手动 `.setId(xxxL)` 也能避免使用雪花算法, 而是自己手动设置的 id

IdType 可选的策略:

```java
    AUTO, //数据库id自增
    NONE, //未设置主键
    INPUT, //手动输入
    ID_WORKER, //默认的全局唯一id  数字表示
    UUID, //全局唯一id uuid
    ID_WORKER_STR;//默认的全局唯一id 字符串表示
```

方法二:

设置全局 id 生成策略:

```yml
mybatis-plus:
    global-config:
        db-config:
            id-type: AUTO
```

## snowflake

涉及到**水平分表**

水平分表适合表行数特别大的表, 水平分表相比垂直分表, 会引入更多的复杂性, 例如要求全局唯一的数据 id 应该如何处理

**主键自增**

1. 以用户 ID 为例, 按照 1000000 的范围大小进行分段, 1~9999999 放到表 1 中, 1000000~1999999 放到表 2 中, 以此类推

2. 复杂点: 分段大小的选取, 太小会导致切分后子表过多, 增加维护复杂度, 太大可能会导致单表依然存在性能问题, 一般分段大小建议再 100 万到 2000 万之间,具体根据业务选取合适的分段大小

3. 优点: 可以随着数据的增加平滑的扩充新的表, 比如: 现有用户 100 万, 如果增加到 1000 万, 只要增加新的表就行了, 原有数据不需要动

4. 缺点: 分布不均匀, 比如: 按 1000 万分表策略, 有可能某个分段实际存储的数据量只有 1 条, 而另一个分段实际存储的数据又 1000 万条

**取模**

1. 同样以用户 ID 为例, 加入一开始规划了 10 个数据库表, 可以简单的用 user_id % 10 的值来表示数据所属的数据库表编号, ID 为 985 的用户放到编号为 5 的子表中, ID 为 10086 的用户放到编号为 6 的子表中

2. 复杂点: 初始表数量的确定, 表数量太多维护比较慢, 表数量太少可能导致单表性能存在问题

3. 优点: 表分布比较均匀

4. 缺点: 扩充新的表很麻烦, 所有数据要重分布

**雪花算法**

雪花算法是由 twitter 公布的分布式主键生成算法, 能够保证不同表的主键的不重复性, 以及相同表的主键有序性 (根据时间, 后生成的一定比前生成的大)

1. 核心思想:

    长度 64bit (Long 类型)

    首先是一个符号位, 1bit 标识, 由于 long 基本类型再 Java 中是带符号的, 最高位是符号位, 正数时 0, 负数时 1, 所以 ID 一般是正数, 最高位是 0

    4bit 时间戳(毫秒级), 存储的时间戳是差值(当前时间戳-开始时间戳), 结果约等于 69.73 年

    10bit 作为及其的 ID (5bit 是数据中心, 5bit 机器 ID, 可以部署在 1024 个节点)

    12bit 作为毫秒内的流水号 (意味着每个节点在每毫秒可以产生 4096 个 ID)

2. 优点: 整体上按照时间自增排序, 并且整个分布式系统内不会产生 ID 碰撞

## change table non-key column

比如将表中的 `name` 列改名为 `user_name`, 并更改实体类的属性名为 `userName`, 发现依然可以正常运行, 因为 MyBatisPlus 可以自动的把下划线改成驼峰

可是如果表里为 `user_name` 实体类属性名为 `name`, 就会报错 (因为不符合规范), 这时候要用注解 `@TableField`

## tablelogic

`@TableLogic`

1. 逻辑删除

    - 物理删除: 真实删除, 将对应数据从数据库中删除, 之后查询不到此条被删除的数据

    - 逻辑删除: 假删除, 将对应数据中代表是否被删除的字段的状态修改为 "被删除的状态", 之后再数据库中仍旧能看到此条数据

    - 使用场景: 可以进行数据恢复

2. 实现逻辑删除

step1: 数据库中创建逻辑删除状态列, 设置默认值为 0

step2: 实体类添加对应字段的属性, 并加上 `@TableLogic`

```java
    @TableLogic
    private Integer isDeleted;
```

此时执行 delete 操作时, 就不是物理删除了, 而成为了修改操作

```java
        List<Long> list = Arrays.asList(1L, 2L, 3L);
        int res = userMapper.deleteBatchIds(list);
        System.out.println(res);
```

输出:

```
==>  Preparing: UPDATE t_user SET is_deleted=1 WHERE uid IN ( ? , ? , ? ) AND is_deleted=0
==> Parameters: 1(Long), 2(Long), 3(Long)
<==    Updates: 3
Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@3e0fbeb5]
3
```

但是此时执行查询操作:

```java
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
```

会输出: (表里 10 个数据输出了 7 个, 因为 `WHERE is_deleted=0`, 其中 3 个值是 1)

```
==>  Preparing: SELECT uid AS id,user_name AS name,age,email,is_deleted FROM t_user WHERE is_deleted=0
==> Parameters:
<==    Columns: id, name, age, email, is_deleted
<==        Row: 4, Sandy, 21, test4@baomidou.com, 0
<==        Row: 5, Billie, 24, test5@baomidou.com, 0
<==        Row: 6, uidtest, 18, uidtest@test.com, 0
<==        Row: 7, uidtest, 18, uidtest@test.com, 0
<==        Row: 8, uidtest, 18, uidtest@test.com, 0
<==        Row: 9, uidtest, 18, uidtest@test.com, 0
<==        Row: 10, uidtest, 18, uidtest@test.com, 0
<==      Total: 7
```

## wrapper

-   Wrapper: 条件构造抽象类, 最顶端父类

    -   AbstractWrapper: 用于查询条件封装, 生成 sql 的 where 条件

        -   QueryWrapper: 查询条件封装

        -   UpdateWrapper: Update 条件封装 (不光可以封装条件, 也能封装字段)

        -   AbstractLambdaWrapper: 使用 Lambda 语法

            -   LambdaQueryWrapper: 用于 Lambda 语法使用的查询 Wrapper

            -   LambdaUpdateWrapper: Lambda 更新封装 Wrapper

**QueryWrapper**

无优先级 update:

```java
        // ==>  Preparing: UPDATE t_user SET user_name=?, email=? WHERE is_deleted=0 AND (age > ? AND user_name LIKE ? OR email IS NULL)
        // ==> Parameters: 小明(String), xiaoming@xiaoming.com(String), 20(Integer), %a%(String)
        // <==    Updates: 1
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("age", 20).like("user_name", "a").or().isNull("email");
```

有优先级 update:

```java
        // ==>  Preparing: UPDATE t_user SET user_name=?, email=? WHERE is_deleted=0 AND (user_name LIKE ? AND (age > ? OR email IS NULL))
        // ==> Parameters: xiaohong(String), xiaohong@xiaohong.com(String), %a%(String), 20(Integer)
        // <==    Updates: 1
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name", "a").and(i -> i.gt("age", 20).or().isNull("email"));
```

## plugin

## pagination plugin

step1:

创建配置类

```java
@Configuration
@MapperScan("cn.sichu.mybatis.plus.mapper")
public class MyBatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```

step2:

使用 `Page<>` 对象返回分页信息

```java
        Page<User> page = new Page<>(2, 3);
        userMapper.selectPage(page, null);
        System.out.println(page);
```

## optimistic lock and pessimistic lock

1. 场景:

    一件商品, 成本价 80 元,售价 100 元, 老板先是通知小李, 把价格增加 50 元, 小李正在玩游戏耽误了一个小时, 正好一个小时后, 老板觉得商品价格应该增加少一点, 又通知小王把降低 30 元

    此时, 小李和小王同时操作商品后台系统, 小李操作时, 先取出价格为 100 元的商品, 小王同时也在操作, 取出的商品价格也是 100 元, 小李增加了 50 元, 并将 100+50=150 元的商品数据存入了数据库, 小王将商品降低了 30 元, 并将 100-30=70 元的数据存入了数据库, 如果没有锁, 小李的操作就完全被小王覆盖了

    现在商品价格是 70 元, 比成本价格低 10 元, 几分钟后, 这个商品很快售出了 1000 件, 亏了 10000 元

2. **乐观锁(optimistic lock)** 和 **悲观锁(pessimistic lock)**

如果是乐观锁, 在场景 1, 小王保存价格前, 会检查下价格是否被人修改过, 如果被修改过了, 则重新取出被修改后的价格, 也就是 150 元的数据, 这样小王的操作会将 120 元存入数据库

如果是悲观锁, 小李取出数据后, 小王只能等待小李操作完后, 才能对价格进行修改, 最终也能保证价格为 120 元

3. 模拟修改冲突

    3.1. 乐观锁实现流程

    先创建一个 `t_product` 表

    ```sql
    create table t_product
    (
    	id bigint(20) not null comment '主键id',
    	name varchar(30) null default null comment '商品名称',
    	price int(11) default 0 comment '价格',
    	version int(11) default 0 comment '乐观锁版本号',
    	primary key (id)
    )
    ```

    数据库中需要添加 `version` 字段

    取出记录时, 获取当前 `version`:

    ```sql
    select id,name,price,version from product where id=1
    ```

    更新时, version + 1, 如果 `where` 语句中的 version 版本不对, 则更新失败

    ```sql
    update product set price=price+50,version=version+1 where id=1 and version=1
    ```

    具体实现:

    现在实体类中为乐观锁 version 添加 `@Version` 注解:

    ```java
    @Data
    public class Product {
    	private Long id;
    	private String name;
    	private Integer price;
    	@Version
    	private Integer version;
    }
    ```

    并在配置类里添加拦截器(interceptor):

    ```java
    interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
    ```

## enum

step1:

创建枚举类, 为数据库中字段对应的属性添加 `@EnumValue` 注解, 并生成有参构造, 添加 `@Getter注解`(因为枚举类一般就是只 get):

```java
@Getter
public enum GenderEnum {
    /**
     *
     */
    MALE(1, "男"), FEMALE(2, "女");

    @EnumValue
    private final Integer gender;
    private final String genderName;

    GenderEnum(Integer gender, String genderName) {
        this.gender = gender;
        this.genderName = genderName;
    }
}
```

step2:

yml 中配置添加枚举类包名扫描目标:

```yml
mybatis-plus:
    type-enums-package: cn.sichu.mybatis.plus.enums
```

## generator

添加依赖:

```xml
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>3.5.1</version>
        </dependency>
        <dependency>
            <groupId>org.freemarker</groupId>
            <artifactId>freemarker</artifactId>
            <version>2.3.31</version>
        </dependency>
```

生成器模板:

```java
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/mybatis_plus?characterEncoding=utf-8&useSSL=false",
                "root", "root").globalConfig(builder -> {
                builder.author("sichu") // 设置作者
                    // .enableSwagger()    // 开启 swagger 模式
                    .fileOverride() // 覆盖已生成文件
                    .outputDir("C://users/sichu/mybatis_plus"); // 指定输出目录
            }).packageConfig(builder -> {
                builder.parent("cn.sichu.mybatis")  // 设置父包名
                    .moduleName("plus")  // 设置父包模块名
                    .pathInfo(
                        Collections.singletonMap(OutputFile.mapperXml, "C://users/sichu/mybatis_plus")); // 设置mapper.xml生成路径
            }).strategyConfig(builder -> {
                builder.addInclude("t_user")    // 设置要生成的表名
                    .addTablePrefix("t_", "c_");    // 设置过滤表前缀
            }).templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板, 默认是velocity引擎模板
            .execute();
```

## multiple data sources

多数据源(multiple data sources) 使用多种场景: 纯粹多库, 读写分离, 一主多从, 混合模式等

场景:

    创建两个库, `mybatis_plus`, `mybatis_plus_1`, 将 `mybatis_plus` 的 `product` 表移动到 `mybatis_plus_1`, 这样每个库一张表, 通过一个测试用例分别获取用户数据与商品数据, 如果获取到说明多库模拟成功

step1: 创建数据库及表

```sql
use `mybatis_plus_1`;
create table product
(
	id bigint(20) not null comment '主键id',
	name varchar(30) null default null comment '商品名称',
	price int(11) default 0 comment '价格',
	version int(11) default 0 comment '乐观锁版本号',
	primary key (id)
);

INSERT INTO mybatis_plus_1.product (id,name,price,version) VALUES
	 (1,'product1',100,1);

```

step2: 添加依赖

```xml
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
            <version>3.5.0</version>
        </dependency>
```

step3: 配置 `application.yml`

```yml
spring:
    datasource:
        dynamic:
            #      设置默认的数据源或者数据源组, 默认值为master
            primary: master
            #      严格匹配数据源, 默认false使用默认数据源, true未匹配到指定数据源时抛异常
            strict: false
            datasource:
                master:
                    url: jdbc:mysql://localhost:3306/mybatis_plus?characterEncoding=utf-8&useSSL=false&useUnicode=true&serverTimezone=UTC
                    driver-class-name: com.mysql.cj.jdbc.Driver
                    username: root
                    password: root
                slave_1:
                    url: jdbc:mysql://localhost:3306/mybatis_plus_1?characterEncoding=utf-8&useSSL=false&useUnicode=true&serverTimezone=UTC
                    driver-class-name: com.mysql.cj.jdbc.Driver
                    username: root
                    password: root
```

step4: 用 `@DS("master")` 为 `UserService` 指定数据源

```java
@DS("master")
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
```

step5: 创建 `ProductService` 及其实现类 `ProductServiceImpl`

```java
public interface ProductService extends IService<Product> {
}
```

```java
@DS("slave_1")
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
}
```

> `@DS(dsName)` 注解也可以放在特定方法上, 存在就近原则, 方法上注解 优先于 类上注解
