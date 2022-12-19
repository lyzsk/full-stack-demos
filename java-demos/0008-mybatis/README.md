# MyBatis

MyBatis 前身 iBatis, 是数据持久层框架, 是对 JDBC 的封装

-   特性:

1. 持久层框架 Persistence framework, 支持定制化 SQL, 存储过程 stored procedures, 以及高级映射 (一对多/多对一)

2. 对 JDBCF 封装, 避免几乎所有代码

3. 用 XML 或者 注解, 将 POJO (Plain Old Java Object) 普通 Java 对象 映射成数据库中的记录 (解耦 decoupling)

4. 半自动的 ORM (Object Relation Mapping) 框架

-   缺点:

1. SQL 编写工作量大, 尤其是字段多, 关联表多的时候

2. SQL 语句依赖数据库, 导致数据库移植性差, 不能随便更换数据库

-   核心接口和类:

`SqlSessionFactoryBuilder` --`build()`--> `SqlSessionFactory` -- `openSession()`--> `SqlSession`

## mapper

mapper 接口就相当于 DAO

mapper 的获取: `sqlSession.getMapper();`

## select query

查询标签 select 必须设置 resultType 或者 resultMap:

`resultType`: 自动映射, 用于属性名和表中字段名一致的的情况

    resultType 本质上是传入类型别名Alias, 所以不区分大小写

`resultMap`: 自定义映射, 用于属性名和表中字段名不一致 或者 一对多/多对一

> resultType 如果在配置文件里设置了 `alias` 不区分大小写

MyBatis 的各种查询功能:

1. 若只有一条返回,

    a. 可以通过实体类 Entity class 接收返回数据

    b. 可以通过 集合 接收返回数据

    c. 可以通过 map 接收返回数据, 而且这种方法用的很多, 比如查 json 数据

2. 若有多条返回,

    a. 可以通过实体类类型的 list 集合接收 (一定不能用实体类, 只能用集合, 否则抛异常`TooManyResultsException`)

    b. 可以通过 map 类型的 list 集合接收

    c. 可以在 mapper 接口的方法上用 `@MapKey("")` 使用数据库中唯一标识的字段作为 key, 返回 map 集合

## mybatis-config.xml

配置文件里的标签必须按照顺序:

```
The content of element type "configuration" must match "(properties?,settings?,typeAliases?,typeHandlers?,objectFactory?,objectWrapperFactory?,reflectorFactory?,plugins?,environments?,databaseIdProvider?,mappers?)".
```

## parameters

获取参数两种方式:

1. `${}`

    本质就是字符串拼接, 需要注意`''`单引号的使用

    而且用字符串拼接存在 sql 注入 的风险

2. `#{}`

    本质是占位符赋值

-   MyBatis 获取参数的各种情况:

1. mapper 接口方法的参数为单个字面量类型, 可以通过 `${}` `#{}`以任意的名称获取参数值

在接口对应的映射 xml 文件里使用 `#{}`, `{}`里传的参数名不重要, 值才重要, 所以 `#{username}` 和 `#{aaa}`能够返回同样的结果

`select * from t_user where username = '${username}'` 手动加单引号后返回结果和 `select * from t_user where username = #{username}` 一样

总而言之尽量用 `#{}`

2. mapper 接口方法的参数为多个时

此时 MyBatis 会将这些参数放在一个 map 集合中, 以两种方式存储:

a. `arg0, arg1...` 为 key, 以 参数 为 value

b. `param1, param2...` 为 key, 以 参数 为 value

所以 `#{}` `${}` 里传的是 key 的值

3. 若 mapper 接口方法的参数有多个时, 根据 2, 可以手动设置一个 map

这种情况下依旧是 `#{}` `${}` 里传 key 的值

4. mapper 接口方法的参数是一个实体类类型的参数

通过 `#{}`, `${}` 以属性的方式访问属性值

这种方式也是最普遍的使用方式

5. 使用 `@Param` 命名参数

这种情况可以使用 `param1, param2...` 和 自己设置的 `@Param()`的 value

> 总结: 总的来说分两种情况比较方便 1. 加 `@Param` 的情况; 2. 加实体类型的情况

## special parameters situation

-   模糊查询 Fuzzy search query

方法 1. 这种情况用 `#{}` 会返回错误的值: `select * from t_user where username like '%?%'`

    换成 `${}`: `select * from t_user where username like '%${username}%'` 则可以正确返回

方法 2. 也可以使用 mysql 的字符串拼接函数: `select * from t_user where username like concat('%', #{username}, '%')`, 这种情况就可以继续使用 `#{}`

方法 3. 也可以使用双引号拼接两个百分号: `select * from t_user where username like "%"#{username}"%"`, 这种方法也是最常用的

-   批量删除 batch deletion

只能使用 `delete from t_user where id in (${ids})`, 返回值是 1, 成功

如果使用 `delete from t_user where id in (#{ids})`, 返回值是 0, 而且在 sql 中执行脚本会报错

-   动态设置表名

只能用 `select * from ${tablename}` 不能用 `#{}`

-   添加功能获取自增的主键

使用场景

`t_clazz(clazz_id,clazz_name)`

`t_student(student_id,student_name,clazz_id)`

添加班级信息

获取新添加的班级的 id

为班级分配学生，即将某学的班级 id 修改为新添加的班级的 id

在`mapper.xml`中设置两个属性

`useGeneratedKeys`：设置使用自增的主键

`keyProperty`：因为增删改有统一的返回值是受影响的行数，因此只能将获取的自增的主键放在传输的参数 user 对象的某个属性中

---

## different name between table and pojo

sql 表里的字段名 和 mapper 里峰驼属性名不一致时 (sql 里是`emp_name`, mapper 里是`empName`):

方法 1:

把 `select * from t_emp` 手动赋予别名 `select eid, emp_name empName, age, gender, email from t_emp`

方法 2:

更改 mybatis 全局配置 `mybatis-config.xml`:

```xml
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
```

方法 3:

更改 mapper.xml, 使用 `ResultMap` 自定义映射关系:

```xml
    <resultMap id="empResultMap" type="Emp">
        <id property="eid" column="eid"></id>
        <result property="empName" column="emp_name"></result>
        <result property="age" column="age"></result>
        <result property="gender" column="gender"></result>
        <result property="email" column="email"></result>
    </resultMap>

    <select id="getAllEmp" resultMap="empResultMap">
        select * from t_emp
    </select>
```

## resultmap

`resultMap` 设置自定义映射关系

属性:

    - id: 自定义映射的唯一标识, 不能重复

    - type: 查询时数据要映射的实体类的类型

子标签:

    - id: 主键的映射关系

    - result: 普通字段的映射关系

    子标签里的属性:

        - property: 映射关系中 实体类的属性名

        - column: 映射关系中 表中数据的字段名

---

## multi to one

多对一查询,

方法 1:

使用级联属性赋值, cascade property (使用 `resultMap` 自定义映射)

```xml
    <resultMap id="empAndDeptResultMapOne" type="emp">
        <id property="eid" column="eid"></id>
        <result property="empName" column="emp_name"></result>
        <result property="age" column="age"></result>
        <result property="gender" column="gender"></result>
        <result property="email" column="email"></result>
        <result property="dept.did" column="did"></result>
        <result property="dept.deptName" column="dept_name"></result>
    </resultMap>
```

关键点在于, 对于 `private Dept dept;` 对象, 获取映射关系的是 `dept.did` 和 `dept.deptName`

这种虽然简单, 但是用的并不多...

方法 2:

使用专门处理 多对一 映射关系的 `association` 标签

```xml
        <association property="dept" javaType="Dept">
            <id property="did" column="did"></id>
            <result property="deptName" column="dept_name"></result>
        </association>
```

方法 3:

依然使用 `association` 标签, 但是使用的是 **分步查询**

以后 多对一/一对多 的情况用这种方法比较多

```xml
    <resultMap id="empAndDeptByStepResultMap" type="Emp">
        <id property="eid" column="eid"></id>
        <result property="empName" column="emp_name"></result>
        <result property="age" column="age"></result>
        <result property="gender" column="gender"></result>
        <result property="email" column="email"></result>
        <association property="dept"
                     select="cn.sichu.mybatis.mapper.DeptMapper.getEmpAndDeptByStepTwo"
                     column="did"></association>
    </resultMap>
```

---

## association tag

`association` 处理多对一映射关系

`property`: 需要处理多对一映射关系的属性名

`javaType`: 该属性的类型

分步查询时用到的属性:

`select`: 设置分步查询的 sql 唯一标识(namespace.sqlid 或者 mapper 接口的全类名.方法名)

`column`: 分步查询的条件, 也就是两步共有的识别字段, 如 did

`fetchType`: lazy | eager, 是否强制取消延迟加载

---

## mybatis lazy loading

分步查询的好处就是 可以帮助实现 MyBatis 的延迟加载/懒加载/lazy loading

`lazyLoadingEnabled`: 延迟加载的全局开关, 开启时, 所有关联对象都会延迟加载

`aggressiveLazyLoading`: 开启时, 任何方法的调用都会加载该对象的所有属性, 否则每个属性都会按需加载

此时就可以实现按需加载，获取的数据是什么，就只会执行相应的 sql。此时可通过 association 和 collection 中的 `fetchType` 属性设置当前的分步查询是否使用延迟加载，fetchType="lazy(延迟加载)|eager(立即加载)"

根据文档:

| Setting               | Valid Values  | Default               |
| --------------------- | ------------- | --------------------- |
| lazyLoadingEnabled    | true \| false | false                 |
| aggressiveLazyLoading | true \| false | false (true <= 3.4.1) |

---

## one to multi

一对多查询, 比如一个部门有多个员工, Emp 用 `List<Emp> Emps` 来存储

方法 1: `collection`

```xml
    <resultMap id="deptAndEmpResultMap" type="Dept">
        <id property="did" column="did"></id>
        <result property="deptName" column="dept_name"></result>
        <collection property="emps" ofType="Emp">
            <id property="eid" column="eid"></id>
            <result property="empName" column="emp_name"></result>
            <result property="age" column="age"></result>
            <result property="gender" column="gender"></result>
            <result property="email" column="email"></result>
        </collection>
    </resultMap>

    <select id="getDeptAndEmp" resultMap="deptAndEmpResultMap">
        select * from t_dept left join t_emp on t_dept.did = t_emp.did
        where t_dept.did = #{did}
    </select>
```

很显然, 使用 `collection` 标签, 我们已经知道 `javaType` 是集合了, 我们需要知道的是里面存储对象是什么类型, 所以需要用 `ofType`

方法 2: 分步查询

```xml
<!-- DeptMapper.xml -->
    <resultMap id="deptAndEmpByStepResultMap" type="Dept">
        <id property="did" column="did"></id>
        <result property="deptName" column="dept_name"></result>
        <collection property="emps"
                    select="cn.sichu.mybatis.mapper.EmpMapper.getDeptAndEmpByStepTwo"
                    column="did"
                    fetchType="lazy"></collection>
    </resultMap>

    <select id="getDeptAndEmpByStepOne" resultMap="deptAndEmpByStepResultMap">
        select * from t_dept where t_dept.did = #{did}
    </select>

<!-- EmpMapper.xml -->
    <select id="getDeptAndEmpByStepTwo" resultType="cn.sichu.mybatis.pojo.Emp">
        select * from t_emp where did = #{did}
    </select>
```

一般来说 分步查询 第一步`resultMap`, 第二步`resultType`, 但是多表联查有多对多关系时可能都用的`resultMap`

---

## mybatis dynamic sql

方法 1: `<if>` 标签

```xml
    <select id="getEmpByConditions" resultType="cn.sichu.mybatis.pojo.Emp">
        select * from t_emp where 1=1
        <if test="empName != null and empName != ''">
            emp_name = #{empName}
        </if>
        <if test="age != null and age != ''">
            and age = #{age}
        </if>
        <if test="gender != null and gender != ''">
            and gender = #{gender}
        </if>
        <if test="email != null and email !=''">
            and email = #{email}
        </if>
    </select>
```

通过在 where 后添加 `1=1` 衡成立条件, 防止报错 `Preparing: select * from t_emp where and emp_name = ? and age = ? and gender = ? and email = ? (BaseJdbcLogger.java:137)`, `org.apache.ibatis.exceptions.PersistenceException`, 帮助拼接 `where and ...` 后的条件

方法 2: `<where>` 标签

当 `<where>` 标签中有内容时, 会自动生成 `where` 关键字, 并且将 sql 语句前多余的 `and` 或 `or` 去掉 (不能将 sql 语句后的 and 和 or 去掉)

当 `<where>` 标签中没有内容时, 此时 `<where>` 标签没有任何效果

```xml
    <select id="getEmpByConditions" resultType="cn.sichu.mybatis.pojo.Emp">
        select * from t_emp
        <where>
            <if test="empName != null and empName != ''">
                emp_name = #{empName}
            </if>
            <if test="age != null and age != ''">
                and age = #{age}
            </if>
            <if test="gender != null and gender != ''">
                and gender = #{gender}
            </if>
            <if test="email != null and email !=''">
                and email = #{email}
            </if>
        </where>
    </select>
```

方法 3: `<trim>` 标签

`prefix | suffix`: 将 `<trim>` 标签中内容前面或后面**添加**指定内容

`suffixOverrides | prefixOverrides`: 将 `<trim>` 标签中内容前面或后面**去掉**指定内容

```xml
    <select id="getEmpByConditions" resultType="cn.sichu.mybatis.pojo.Emp">
        select * from t_emp
        <trim prefix="where" suffixOverrides="and|or">
            <if test="empName != null and empName != ''">
                emp_name = #{empName} and
            </if>
            <if test="age != null and age != ''">
                age = #{age} or
            </if>
            <if test="gender != null and gender != ''">
                gender = #{gender} and
            </if>
            <if test="email != null and email !=''">
                email = #{email}
            </if>
        </trim>
    </select>
```

方法 4: `<choose>, <when>, <otherwise>` 标签

这一套标签相当于 `if...else`

```xml
    <select id="getEmpByChoose" resultType="cn.sichu.mybatis.pojo.Emp">
        select * from t_emp
        <where>
            <choose>
                <when test="empName != null and empName != ''">
                    emp_name = #{empName}
                </when>
                <when test="age != null and age != ''">
                    age = #{age}
                </when>
                <when test="gender != null and gender != ''">
                    gender = #{gender}
                </when>
                <when test="email != null and email != ''">
                    email = #{email}
                </when>
                <otherwise>
                    did = 2
                </otherwise>
            </choose>
        </where>
    </select>
```

方法 5: `<foreach>`

a. 用 `<foreach>` 实现批量删除

> 建议: 除了 实体类对象 和 Map 集合 的情况, 其他情况都加 `@Param()` 来访问

方法 5. a:

```java
    /**
     * 通过数组实现批量删除
     */
    int deleteMultiByArray(@Param("eids") Integer[] eids);
```

```xml
    <delete id="deleteMultiByArray">
        delete from t_emp where eid in
        (
        <foreach collection="eids" item="eid" separator="," open="(" close=")">
            #{eid}
        </foreach>
        )
    </delete>
```

方法 5. b:

```xml
    <delete id="deleteMultiByArrayOne">
        delete from t_emp where eid in
        (
        <foreach collection="eids" item="eid" separator="," open="(" close=")">
            #{eid}
        </foreach>
        )
    </delete>
```

b. 用 `<foreach>` 实现批量添加

```java
    /**
     * 通过List集合实现批量添加
     */
    int insertMultiByList(@Param("emps") List<Emp> emps);
```

```xml
    <insert id="insertMultiByList">
        insert into t_emp values
        <foreach collection="emps" item="emp" separator=",">
            (null, #{emp.empName}, #{emp.age}, #{emp.gender}, #{emp.email}, null)
        </foreach>
    </insert>
```

方法 6: `<sql>` 标签

使用 `<sql>` 标签记录常用的 sql 字段 (因为日常开发中很少用 `*` 来作条件, 而是具体的字段)

使用 `<include>` 来引用 `<sql>` 标签的 id

```xml
    <sql id="empColumns">eid,emp_name,age,gender,email</sql>

    <select id="getEmpByConditions" resultType="cn.sichu.mybatis.pojo.Emp">
        select <include refid="empColumns"></include> from t_emp
```

---

## cache in mybatis

## level 1 cache

一级缓存 L1 cache 是 `SqlSession` 级别的, 通过同一个 `SqlSession` 查询的数据会被缓存, 下次查询相同的数据, 会直接从缓存中获取, 没有才会从数据库中取

一级缓存失效的四种情况:

1. 不同的 SqlSession 对应不同的一级缓存

2. 同一个 SqlSession 但是查询条件不同

3. 同一个 SqlSession 两次查询期间执行了任何一次增删改操作

4. 同一个 SqlSession 两次查询期间手动情况了缓存 `sqlSession.clearCache();`

## level 2 cache

二级缓存 L2 Cache 是 `SqlSessionFactory` 级别, 通过同一个 `SqlSessionFactory` 创建的 `SqlSession` 查询的结果会被缓存, 此后若再次执行相同查询语句, 结果会从缓存中获取

二级缓存开启条件:

1. 在核心配置文件中，设置全局配置 (`<settings>` 标签里) 属性 cacheEnabled="true"，默认为 true，不需要设置

2. 在映射文件中设置标签 `<cache />`

3. 二级缓存必须在 SqlSession 关闭或提交之后有效 (每次查询后手动添加 `sqlSession.close();`)

4. 查询的数据所转换的实体类类型必须实现序列化的接口

使二级缓存失效的情况：

1. 两次查询之间执行了任意的增删改，会使一级和二级缓存同时失效

二级缓存的相关配置 (`<cache />`标签的配置)

`eviction`: 缓存回收策略

> LRU（Least Recently Used） – 最近最少使用的：移除最长时间不被使用的对象。

> FIFO（First in First out） – 先进先出：按对象进入缓存的顺序来移除它们。

> SOFT – 软引用：移除基于垃圾回收器状态和软引用规则的对象。

> WEAK – 弱引用：更积极地移除基于垃圾收集器状态和弱引用规则的对象。

> 默认的是 LRU

`flushInterval`: 刷新间隔，单位毫秒

> 默认情况是不设置，也就是没有刷新间隔，缓存仅仅调用语句（增删改）时刷新

`size`: 引用数目，正整数

> 代表缓存最多可以存储多少个对象，太大容易导致内存溢出

`readOnly`: 只读，true/false

> true：只读缓存；会给所有调用者返回缓存对象的相同实例。因此这些对象不能被修改。这提供了很重要的性能优势。

> false：读写缓存；会返回缓存对象的拷贝（通过序列化）。这会慢一些，但是安全，因此默认是 false

---

## mybatis cache queries order

先查询二级缓存，因为二级缓存中可能会有其他程序已经查出来的数据，可以拿来直接使用

如果二级缓存没有命中，再查询一级缓存

如果一级缓存也没有命中，则查询数据库

SqlSession 关闭之后，一级缓存中的数据会写入二级缓存

---

## mybatis ehcache

1. 配置 pom 依赖

```xml
        <!-- Mybatis EHCache整合包 -->
        <dependency>
            <groupId>org.mybatis.caches</groupId>
            <artifactId>mybatis-ehcache</artifactId>
            <version>1.2.1</version>
        </dependency>
        <!-- slf4j日志门面的一个具体实现 -->
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
        </dependency>
```

2. 创建`ehcache.xml` 模板:

```xml
<?xml version="1.0" encoding="utf-8" ?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="../config/ehcache.xsd">
    <!-- 磁盘保存路径 -->
    <diskStore path="C:\users\sichu\dev\IDEA-Workspace\ehcache"/>
    <defaultCache
            maxElementsInMemory="1000"
            maxElementsOnDisk="10000000"
            eternal="false"
            overflowToDisk="true"
            timeToIdleSeconds="120"
            timeToLiveSeconds="120"
            diskExpiryThreadIntervalSeconds="120"
            memoryStoreEvictionPolicy="LRU">
    </defaultCache>
</ehcache>
```

3. 设置二级缓存的类型 (第三方库只能更改二级缓存 不能改 mybatis 的一级缓存)

在 `xxxMapeer.xml` 里添加:

```xml
<cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
```

4. 加入 logback 日志

存在 SLF4J 时，作为简易日志的 log4j 将失效，此时我们需要借助 SLF4J 的具体实现 logback 来打印日志。创建 logback 的配置文件`logback.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="true">
    <!-- 指定日志输出的位置 -->
    <appender name="STDOUT"
              class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <!-- 日志输出的格式 -->
            <!-- 按照顺序分别是：时间、日志级别、线程名称、打印日志的类、日志主体内容、换行 -->
            <pattern>[%d{HH:mm:ss.SSS}] [%-5level] [%thread] [%logger] [%msg]%n</pattern>
        </encoder>
    </appender>
    <!-- 设置全局日志级别。日志级别按顺序分别是：DEBUG、INFO、WARN、ERROR -->
    <!-- 指定任何一个日志级别都只打印当前级别和后面级别的日志。 -->
    <root level="DEBUG">
        <!-- 指定打印日志的appender，这里通过“STDOUT”引用了前面配置的appender -->
        <appender-ref ref="STDOUT"/>
    </root>
    <!-- 根据特殊需求指定局部日志级别 -->
    <logger name="cn.sichu.mybatis.mapper" level="DEBUG"/>
</configuration>

```

---

## mybatis mbg

正向工程：先创建 Java 实体类，由框架负责根据实体类生成数据库表。Hibernate 是支持正向工程的

逆向工程：先创建数据库表，由框架负责根据数据库表，反向生成如下资源：

-   Java 实体类

-   Mapper 接口

-   Mapper 映射文件

## create mbg step by step

step1: 添加插件

```xml
    <build>
        <!-- 构建过程中用到的插件 -->
        <plugins>
            <!-- 具体插件，逆向工程的操作是以构建过程中插件形式出现的 -->
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.1</version>
                <!-- 插件的依赖 -->
                <dependencies>
                    <!-- 逆向工程的核心依赖 -->
                    <dependency>
                        <groupId>org.mybatis.generator</groupId>
                        <artifactId>mybatis-generator-core</artifactId>
                        <version>1.3.3</version>
                    </dependency>
                    <!-- 数据库连接池 -->
                    <dependency>
                        <groupId>com.mchange</groupId>
                        <artifactId>c3p0</artifactId>
                        <version>0.9.5.4</version>
                    </dependency>
                    <!-- MySQL驱动 -->
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>8.0.28</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
```

step2: 创建 MyBatis 的核心配置文件 `mybatis-config.xml`

step3: 创建逆向工程的配置文件 `generatorConfig.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <!--
    targetRuntime: 执行生成的逆向工程的版本
    MyBatis3Simple: 生成基本的CRUD（清新简洁版）
    MyBatis3: 生成带条件的CRUD（奢华尊享版）
    -->
    <context id="DB2Tables" targetRuntime="MyBatis3Simple">
        <!-- 数据库的连接信息 -->
        <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/mybatis"
                        userId="root"
                        password="root">
        </jdbcConnection>
        <!-- javaBean的生成策略-->
        <javaModelGenerator targetPackage="cn.sichu.mybatis.pojo" targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>
        <!-- SQL映射文件的生成策略 -->
        <sqlMapGenerator targetPackage="cn.sichu.mybatis.mapper"
                         targetProject=".\src\main\resources">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>
        <!-- Mapper接口的生成策略 -->
        <javaClientGenerator type="XMLMAPPER"
                             targetPackage="cn.sichu.mybatis.mapper" targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>
        <!-- 逆向分析的表 -->
        <!-- tableName设置为*号，可以对应所有表，此时不写domainObjectName -->
        <!-- domainObjectName属性指定生成出来的实体类的类名 -->
        <table tableName="t_emp" domainObjectName="Emp"/>
        <table tableName="t_dept" domainObjectName="Dept"/>
    </context>
</generatorConfiguration>
```

如果 `"http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">` 报红可以无视, 实测无影响

step4: 执行 MBG 插件的 generate 目标

**注意**: MBG 并不会帮助生成 `constructor`, `toString` 方法, 需要手动给实体类添加

---

## QBC

`selectByExample`：按条件查询，需要传入一个 example 对象或者 null；如果传入一个 null，则表示没有条件，也就是查询所有数据

`example.createCriteria().xxx`：创建条件对象，通过 andXXX 方法为 SQL 添加查询添加，每个条件之间是 and 关系

`example.or().xxx`：将之前添加的条件通过 or 拼接其他条件

> 因为每次链式结构返回的对象都是 `Criteria` 对象, 所以可以使用链式结构 method chaining

`updateByPrimaryKey`：通过主键进行数据修改，如果某一个值为 null，也会将对应的字段改为 null

`updateByPrimaryKeySelective()`：通过主键进行选择性数据修改，如果某个值为 null，则不修改

---

## pagehelper

分页插件使用步骤:

step1: 添加依赖

```xml
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
            <version>5.2.0</version>
        </dependency>
```

step2: 在 mybatis 核心配置文件中配置分页插件

```xml
    <plugins>
        <!--   设置分页插件    -->
        <plugin interceptor="com.github.pagehelper.PageInterceptor"></plugin>
    </plugins>
```

从 `PageInterceptor` 可以看出, 这就是一个拦截器

分页插件的使用:

```java
    /**
     * limit关键词 index, pageSize,
     * index: 当前页的起始索引
     * pageSize: 每页显示的条数
     * pageNum: 当前页的页码
     * index = (pageNum - 1) * pageSize
     */
```

开启分页功能: `PageHelper.startPage()`

分页相关数据:

方法一: 直接输出

```java
            PageHelper.startPage(3, 5);
            List<Emp> list = mapper.selectByExample(null);
            list.forEach(System.out::println);
```

方法二: 使用 PageHelper 自带的 `PageInfo<>`

```java
            PageInfo<Emp> page = new PageInfo<>(list, 3);
            System.out.println(page);
```
