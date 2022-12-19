# 01-jpa-hibernate

添加依赖:

```xml
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>5.4.32.Final</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.28</version>
        </dependency>
```

创建实体类 `Customer`

Hibernate 是一个 code first 的 ORM 框架, 先写代码再生成数据库

添加配置文件 `hibernate.cfg.xml`

```xml
<?xml version = "1.0" encoding = "utf-8"?>
<!DOCTYPE hibernate-configuration SYSTEM
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>

        <!--        配置数据库连接信息-->
        <property name="connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="connection.url">jdbc:mysql://localhost:3306/springdata_jpa?characterEncoding=utf-8&amp;useSSL=false&amp;useUnicode=true&amp;serverTimezone=UTC</property>
        <property name="connection.username">root</property>
        <property name="connection.password">root</property>

        <!--        允许显示sql语句, 默认false-->
        <property name="show_sql">true</property>
        <!--        是否格式化sql, 默认false-->
        <property name="format_sql">true</property>
        <!--        表的生成策略
                        none        默认值, 不自动生成
                        update      如果没有表会自动创建, 有会检查更新
                        create      每次运行都会创建-->
        <property name="hbm2ddl.auto">update</property>

        <!--        配置数据库方言-->
        <property name="dialect">org.hibernate.dialect.MySQL8Dialect</property>

        <!--        指定哪些pojo需要ORM映射-->
        <mapping class="cn.sichu.pojo.Customer"/>

    </session-factory>
</hibernate-configuration>
```

创建数据库 `springdata_jpa` (虽然表可以自动生成但是数据库要手动创建)

Hibernate 是用 `SessionFactory` 创建持久化 session 的:

```java
    // 类似 MyBatis 的 SqlSessionFactory
    private SessionFactory sf;

    @Before
    public void init() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("/hibernate.cfg.xml").build();
        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }
```

然后每次使用的时候开启 session, 并开始事务

```java
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
```

## hibernate CRUD

`find(Class<T> entityClass, Object primaryKey)` 立即查询

`load(Class<T> var1, Serializable var2);` 延迟查询 lazy loading

## JPA

`resources` 目录下添加 `META-INF/persistence.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             version="2.0"
             xmlns="http://java.sun.com/xml/ns/persistence"
             xsi:schemaLocation="http://java.sun.com/xml/ns/persistence
             http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd">

    <!--    persistence-unit 持久化单元:
                    name: 持久化单元名称
                    trasaction-type: 事务管理方式
                            JTA: 分布式事务管理
                            RESOURCE_LOCAL: 本地事务管理-->
    <persistence-unit name="hibernateJPA" transaction-type="RESOURCE_LOCAL">

        <!--        JPA的实现方式-->
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>

        <!--        需要进行ORM的POJO类-->
        <class>cn.sichu.pojo.Customer</class>

        <properties>
            <property name="javax.persistence.jdbc.user" value="root"/>
            <property name="javax.persistence.jdbc.password" value="root"/>
            <property name="javax.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
            <!--            <property name="javax.persistence.jdbc.url"-->
            <!--                      value="jdbc:mysql://localhost:3306/springdata_jpa"/>-->
            <property name="javax.persistence.jdbc.url"
                      value="jdbc:mysql://localhost:3306/springdata_jpa?characterEncoding=utf-8&amp;useSSL=false&amp;useUnicode=true&amp;serverTimezone=UTC"/>

            <!--            hibernate独有的配置信息-->
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.MySQL8Dialect"/>
        </properties>
    </persistence-unit>
</persistence>
```

JPA 不是通过 SessionFactory 创建 session 的, 而是通过 `EntityManagerFactory` 来创建, `createEntityManagerFactory()` 里传的 `persistenceUnitName` 就是在 `persistence.xml` 里配置的持久化单元名称

```java
    private EntityManagerFactory factory;

    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("hibernateJPA");
    }
```

每次使用的时候, 通过工厂对象 `createEntityManager()`, 创建和数据库沟通的桥梁, 然后通过 `EntityManager` 对象创建事务

```java
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        transaction.commit();
```

## JPA CRUD

-   JPA 的 read

        普通查询用: `find(Class<T> entityClass, Object primaryKey)`

        延迟查询用 `getReference(Class<T> entityClass,

    Object primaryKey)`

-   JPA 的 update 操作是用 `merge(T entity)`

    如果指定了 pk , 会先查询再修改

    如果没指定 pk, 会变成插入操作

-   JPA 的 delete 操作用 `remove(Object entity)`

    但是不能删除 `detached instance` 游离状态的数据, 也就是 new 出来的对象, 否则会报异常

    ```java
    Customer customer = new Customer();
    customer.setCustomerId(5L);
    // java.lang.IllegalArgumentException: Removing a detached instance cn.sichu.pojo.Customer#5
    entityManager.remove(customer);
    ```

    应该要先查询再删除

    ```java
        // Hibernate: select customer0_.cust_id as cust_id1_0_0_, customer0_.cust_address as cust_add2_0_0_, customer0_.cust_name as cust_nam3_0_0_ from cst_customer customer0_ where customer0_.cust_id=?
        // Hibernate: delete from cst_customer where cust_id=?
        Customer customer = entityManager.find(Customer.class, 5L);
        entityManager.remove(customer);
        transaction.commit();
    ```

## Entity states in JPA and Hibernate

1. New (Transient)

临时状态, 没有与 entityManager 发生关系, 没有被持久化, 不处于 entityManager 中的对象

2. Persistent (Managed)

与 entityManager 发生关系, 已经被持久化, 您可以持久化状态当作实实在在的数据库记录

3. Detached (Unmanaged)

游离状态就是提交到数据库后, 事务 commit 后的实体状态, 因为事务已经提交了, 此时实体的属性任你如何改变, 也不会同步到数据库, 因为游离状态不在持久化上下文中

4. Removed (deleted)

执行 remove 方法, 事务提交之前

---

# spring data jpa

> 注意, 官方文档里的 `domain` 一律换成 `pojo` 就行了, 记得加上 `@Entity` 和 `@Table` 注解

step1. 从官网 https://docs.spring.io/spring-data/jpa/docs/2.7.6/reference/html/#dependencies 复制粘贴依赖 (放在 parent 的 pom 中):

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.data</groupId>
      <artifactId>spring-data-bom</artifactId>
      <version>2021.2.6</version>
      <scope>import</scope>
      <type>pom</type>
    </dependency>
  </dependencies>
</dependencyManagement>
```

step2. 在子模块中添加依赖:

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-jpa</artifactId>
        </dependency>

        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>5.4.32.Final</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.28</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.2.8</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.3.24</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

step3. 创建 pojo 实体类对象 和 repository 对象:

```java
@Entity
@Table(name = "cst_customer")
public class Customer {}
```

```java
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
```

step4. 用 namespace 方法时就配置 xml (也可以使用 JavaConfig 的方式, 用注解配置)

-   xml 方式:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:jpa="http://www.springframework.org/schema/data/jpa"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
    https://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/data/jpa
    https://www.springframework.org/schema/data/jpa/spring-jpa.xsd
    http://www.springframework.org/schema/tx
    http://www.springframework.org/schema/tx/spring-tx.xsd">

    <!--    用于整合jpa @EnableJpaRepositories-->
    <jpa:repositories base-package="cn.sichu.repositories"
                      entity-manager-factory-ref="entityManagerFactory"
                      transaction-manager-ref="transactionManager"/>

    <!--    entityManagerFactory-->
    <bean name="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
        <property name="jpaVendorAdapter">
            <!--            Hibernate实现-->
            <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
                <!--                true对应的 hbm2ddl.auto 的 update-->
                <property name="generateDdl" value="true"/>
                <property name="showSql" value="true"/>
            </bean>
        </property>

        <!--        设置实体类的包-->
        <property name="packagesToScan" value="cn.sichu.pojo"/>
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!--    数据源-->
    <bean name="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="url"
                  value="jdbc:mysql://localhost:3306/springdata_jpa?characterEncoding=utf-8&amp;useSSL=false&amp;useUnicode=true&amp;serverTimezone=UTC"/>
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
        <property name="username" value="root"/>
        <property name="password" value="root"/>
    </bean>

    <!--    声明式事务-->
    <bean name="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
        <property name="entityManagerFactory" ref="entityManagerFactory"/>
    </bean>

    <!--    启动注解方式的声明式事务-->
    <tx:annotation-driven transaction-manager="transactionManager"/>
</beans>
```

使用时:

```java
@ContextConfiguration("/spring.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringDataJpaTest {}
```

-   JavaConfig 方式:

```java
@Configuration
@EnableJpaRepositories(basePackages = "cn.sichu.repositories")
@EnableTransactionManagement
public class SpringDataJPAConfig {

    @Bean
    public DataSource dataSource() {
        // EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        // return builder.setType(EmbeddedDatabaseType.HSQL).build();
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("root");
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        druidDataSource.setUrl(
            "jdbc:mysql://localhost:3306/springdata_jpa?characterEncoding=utf-8&useSSL=false&useUnicode=true&serverTimezone=UTC");
        return druidDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("cn.sichu.pojo");
        factory.setDataSource(dataSource());
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}
```

> 注意, `setUrl()` 的时候记得要删掉从 xml 里复制过来的 `&amp;` 改成普通的 `&`

使用时:

```java
@ContextConfiguration(classes = SpringDataJPAConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringDataJpaTest {}
```

## spring data jpa crud

-   Create and Update

    Spring Data JPA 的 `save()` 简化了插入和更新的功能, 相当于 JPA 里用来 update 操作的的 `merge(T entity)`

    ```java
        @Test
        public void testC() {
            Customer customer = new Customer();
            customer.setId(4L);
            customer.setName("王五");
            // 相当于jpa的merge, 没指定id是insert, 指定id是update
            customerRepository.save(customer);
        }
    ```

-   Delete

    即使是 detached 游离状态的对象, Spring Data JPA 底层也会先查询再删除

    ```java
        @Test
        public void testD() {
            Customer customer = new Customer();
            customer.setId(4L);
            customer.setName("王五");
            // Hibernate: select customer0_.cust_id as cust_id1_0_0_, customer0_.cust_address as cust_add2_0_0_, customer0_.cust_name as cust_nam3_0_0_ from cst_customer customer0_ where customer0_.cust_id=?
            // Hibernate: delete from cst_customer where cust_id=?
            // 即使是 detached 游离状态的对象, Spring Data JPA 底层也会先查询再删除
            customerRepository.delete(customer);
        }
    ```

## spring data jpa repositories

`CrudRepository<T, ID>` `ctrl + h` 可以看到下面还有一个继承类 `PagingAndSortingRepository<T, ID> extends CrudRepository<T, ID>`

所以如果要是用 分页, 排序 功能可以把 `public interface CustomerRepository extends CrudRepository<Customer, Long>` 改成 `public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long>`

但是 原生的 crudrepository 提供的功能比较简单, 所以可以通过 jpql(原生 SQL)实现复杂功能

-   自定义 jpql

    在 repository 接口里添加方法

    在方法上使用 `@Query(sql_code)` 来调用

    ```java
    // @Query("FROM Customer where name=?1")
    // List<Customer> findCustomerByName(String name);

    @Query("FROM Customer where name=:name")
    List<Customer> findCustomerByName(@Param("name") String name);
    ```

    -   `@Query()`参数设置:

        1. 索引: `?数字`

        2. 名字: `:参数名`, 结合 `@Param 注解参数`

    -   如果是 增删改 操作:

        1. 要加上 `@Transaction` 注解支持事务

        2. 要加上 `@Modifying` 声明操作类型

        3. 通常两个注解是放在业务逻辑上的, 演示的时候就放在 repository 接口里了

        ```java
        @Query("UPDATE Customer c set c.name=:name where c.id=:id")
        @Transactional
        @Modifying
        int updateCustomerById(@Param("name") String name, @Param("id") Long id);
        ```

        -   如果是 `INSERT`, JPQL 理论上是不支持插入语句的, 但是 Hibernate 实现了一种伪插入方式, 通过 select 来 insert (insert into ... select ...)

        ```java
        @Transactional
        @Modifying
        @Query("INSERT INTO Customer(name) SELECT c.name FROM Customer c WHERE c.id=?1")
        int insertCustomerBySelect(Long id);
        ```

    -   如果想写原生 sql, 直接在 `@Query()` 里声明 `nativeQuery = true`

-   自定义方法名:

    官方 JPQL 支持 repository query keywords: https://docs.spring.io/spring-data/jpa/docs/2.7.6/reference/html/#repository-query-keywords

    写的时候对着官方的表写就行了

但是这两种方法, 都不能实现动态查询...

-   Dynamic query

    1.  Query by Example

        只支持查询

        i. 不支持嵌套或分组的属性约束, i.e. firstname = ? 0 or (firstname = ? 1 and lastname = ? 2)

        ii. 只支持字符串 start/contains/ends/regex 匹配和其他属性类型的精确匹配

        实现:

        直接在 `PagingAndSortingRepository<>` 后再添加一个继承 `QueryByExampleExecutor<>`:

        ```java
        public interface CustomerQueryByExampleRepository extends PagingAndSortingRepository<Customer, Long>,
            QueryByExampleExecutor<Customer> {
        }
        ```

        使用的时候用 springframework 里的 `Example<>` 构造查询条件, 并传入 repository 的方法中:

        ```java
        @Test
        public void testExample() {
            Customer customer = new Customer();
            customer.setName("张三");
            // 通过Example构建查询条件
            Example<Customer> example = Example.of(customer);
            List<Customer> list = new ArrayList<>();
            Iterable<Customer> all = repository.findAll(example);
            all.forEach(list::add);
            System.out.println(list);
        }
        ```

        也可以用 `ExampleMatcher` 创建匹配器, 进一步设置:

        ```java
        @Test
        public void testExampleMatcher() {
            Customer customer = new Customer();
            customer.setName("张三");
            customer.setAddress("JING");
            // 通过匹配器 对条件行为配置
            ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("name").withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.ENDING);
            // 通过Example构建查询条件
            Example<Customer> example = Example.of(customer, matcher);
            List<Customer> list = new ArrayList<>();
            Iterable<Customer> all = repository.findAll(example);
            all.forEach(list::add);
            System.out.println(list);
        }
        ```

        注意: `.withStringMatcher(ExampleMatcher.StringMatcher.ENDING)` 会对所有字符串进行结尾匹配

        如果要精准匹配 某一个 属性, 需要使用 `withMatcher()`:

        ```java
        ExampleMatcher matcher = ExampleMatcher.matching()
            // .withIgnorePaths("name")
            .withIgnoreCase()
            // .withStringMatcher(ExampleMatcher.StringMatcher.ENDING)
            .withMatcher("address", m -> m.endsWith());
        ```

        也可以通过调用静态的 `endsWith()` 方法, 不需要 lambda 表达式, 而且还能在静态调用后链式写法(method chaining)加方法

        ```java
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("name")
            // .withIgnoreCase()
            // .withStringMatcher(ExampleMatcher.StringMatcher.ENDING)
            // .withMatcher("address", m -> m.endsWith())
            .withMatcher("address", endsWith().ignoreCase());
        ```

    2.  Specifications

        继承 `JpaSpecificationExecutor<>` 接口;

        ```java
        public interface CustomerSpecificationRepository
            extends PagingAndSortingRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
        }
        ```

        通过创建 Specification 对象来实现查询:

        ```java
        List<Customer> all = repository.findAll(new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                // root, 可以理解成 from, 获取column
                // criteriaBuilder, 可以理解成 where, 设置条件 (< , >, in)
                // query (组合 orderby, where)
                return null;
            }
        });
        ```

        ```java
        @Test
        public void testFindAll() {
            List<Customer> all = repository.findAll(new Specification<Customer>() {
                @Override
                public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    // root, 可以理解成 from, 获取column
                    // query (组合 orderby, where)
                    // criteriaBuilder, 可以理解成 where, 设置条件 (< , >, in)
                    Path<Long> id = root.get("id");
                    Path<String> name = root.get("name");
                    Path<String> address = root.get("address");
                    Predicate predicate1 = criteriaBuilder.equal(address, "beijing");
                    Predicate predicate2 = criteriaBuilder.greaterThan(id, 2L);
                    // Hibernate: select customer0_.cust_id as cust_id1_0_, customer0_.cust_address as cust_add2_0_, customer0_.cust_name as cust_nam3_0_ from cst_customer customer0_ where customer0_.cust_address=? and customer0_.cust_id>2
                    // [Customer{id=7, name='张三', address='BEIJING'}, Customer{id=9, name='田七', address='beiJING'}]
                    // 可以看出这个 equal() 方法忽略了大小写
                    Predicate predicateAll1 = criteriaBuilder.and(predicate1, predicate2);
                    CriteriaBuilder.In<String> in = criteriaBuilder.in(name).value("张三").value("李四").value("王五");
                    Predicate predicateAll2 = criteriaBuilder.and(predicateAll1, in);
                    return predicateAll2;
                }
            });
            System.out.println(all);
        }
        ```

        可以看出 Specification 很复杂, 是**为了处理简单业务逻辑而生的接口, 而不是给复杂场景用的**, 换句话说...用 Specification 的场景 **非常少**

        比如稍微复杂点的, 从外部接收数值的情况:

        ```java
        @Test
        public void testDynamicFindAll() {
            Customer customer = new Customer();
            customer.setId(1L);
            customer.setName("张三, 李四, 田七, 周八");
            customer.setAddress("beijing");
            repository.findAll(new Specification<Customer>() {
                @Override
                public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    Path<Long> id = root.get("id");
                    Path<String> name = root.get("name");
                    Path<String> address = root.get("address");
                    List<Predicate> list = new ArrayList<>();
                    if (!StringUtils.isEmpty(customer.getAddress())) {
                        list.add(criteriaBuilder.equal(address, "beijing"));
                    }
                    if (customer.getId() > -1) {
                        list.add(criteriaBuilder.equal(id, 1L));
                    }
                    if (!StringUtils.isEmpty(customer.getName())) {
                        CriteriaBuilder.In<String> inList = criteriaBuilder.in(name).value("张三").value("李四").value("田七");
                        list.add(inList);
                    }
                    Predicate predicate = criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
                    return predicate;
                }
            });
        }
        ```

        非常难用, 非常不完美

    3.  QueryDSL

            http://querydsl.com/

            QueryDSL 是基于 ORM 框架或 SQL 平台上的一个**通用查询框架**, 不属于 Spring ! 也不属于 Hibernate ! 借助 QueryDSL 可以实现**以通用 API 方式构建查询**, 而且在 mysql mongodb, etc 都能用

            JPA 是 QueryDSL 的集成技术, 是 JPQL 和 Criteria 查询的替代方法

            QueryDSL 需要一个接口 `QueryDslPredicateExecutor`, 这个接口定义了很多查询方法

            使用方法也和其他方法一样, 在 repository 接口后添加接口:

            ```java
            public interface CustomerQueryDSLRepository
                extends PagingAndSortingRepository<Customer, Long>, QuerydslPredicateExecutor<Customer> {
            }
            ```

            添加依赖:

            ```xml
                <dependency>
                    <groupId>com.querydsl</groupId>
                    <artifactId>querydsl-apt</artifactId>
                    <version>5.0.0</version>
                </dependency>

                <dependency>
                    <groupId>com.querydsl</groupId>
                    <artifactId>querydsl-jpa</artifactId>
                    <version>5.0.0</version>
                </dependency>

            <build>
                <plugins>
                    <plugin>
                        <groupId>com.mysema.maven</groupId>
                        <artifactId>apt-maven-plugin</artifactId>
                        <version>1.1.3</version>
                        <executions>
                            <execution>
                                <goals>
                                    <goal>process</goal>
                                </goals>
                                <configuration>
                                    <outputDirectory>target/generated-sources</outputDirectory>
                                    <processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor>
                                </configuration>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
            ```

            具体怎么用 Predicate 对象查询 JPA 官网: https://docs.spring.io/spring-data/jpa/docs/2.7.6/reference/html/#repository-query-keywords

            但是官网的例子优点特殊...用的是一个 Q 类 + 对应实体名 (qery type 查询实体), 不知道怎么创建

            已经有 `querydsl-jpa` 和 `querydsl-apt` 后直接 maven -> compile, 就能生成对应的 Q类对象到 target 文件夹中, 然后设置对应文件夹为resources (也可以直接把编译之后的.java文件复制出来)

            然后就能使用 Predicate 接口了

            ```java
            @Test
            public void testR() {
                QCustomer customer = QCustomer.customer;
                // 通过id查找
                BooleanExpression eq = customer.id.eq(1L);
                Optional<Customer> result = repository.findOne(eq);
                System.out.println(result.get());
            }
            ```

            ```java
            @Test
            public void testIn() {
                QCustomer customer = QCustomer.customer;
                // Hibernate: select customer0_.cust_id as cust_id1_0_, customer0_.cust_address as cust_add2_0_, customer0_.cust_name as cust_nam3_0_ from cst_customer customer0_ where (customer0_.cust_name in (? , ?)) and customer0_.cust_id>? and customer0_.cust_address=?
                // [Customer{id=7, name='张三', address='BEIJING'}, Customer{id=9, name='田七', address='beiJING'}]
                // 也就说明这个 eq() 是忽略大小写的
                BooleanExpression expression =
                    customer.name.in("张三", "田七").and(customer.id.gt(2L)).and(customer.address.eq("beijing"));
                List<Customer> list = new ArrayList<>();
                repository.findAll(expression).forEach(list::add);
                System.out.println(list);
            }
            ```

            QueryDSL 也可以动态查询:

            ```java
            @Test
            public void testDynamicQuery() {
                Customer customer = new Customer();
                customer.setId(1L);
                customer.setName("张三, 田七");
                QCustomer qcustomer = QCustomer.customer;
                // 初始条件 类似于1=1
                BooleanExpression expression = qcustomer.isNotNull().or(qcustomer.isNotNull());
                expression = customer.getId() > -1 ? expression.and(qcustomer.id.gt(customer.getId())) : expression;
                expression = StringUtils.isEmpty(customer.getName()) ? expression :
                    expression.and(qcustomer.name.in(customer.getName().split(",")));
                expression = StringUtils.isEmpty(customer.getAddress()) ? expression :
                    expression.and(qcustomer.address.eq(customer.getAddress()));
                List<Customer> list = new ArrayList<>();
                repository.findAll(expression).forEach(list::add);
                // Hibernate: select customer0_.cust_id as cust_id1_0_, customer0_.cust_address as cust_add2_0_, customer0_.cust_name as cust_nam3_0_ from cst_customer customer0_ where customer0_.cust_name in (? , ?)
                // [Customer{id=1, name='张三', address='beijing'}, Customer{id=7, name='张三', address='BEIJING'}]
                // 写错了, 没有用 expression.and()
                System.out.println(list);
                System.out.println("\n" + repository.findAll(expression));
                // [Customer{id=7, name='张三', address='BEIJING'}]
                // Hibernate: select customer0_.cust_id as cust_id1_0_, customer0_.cust_address as cust_add2_0_, customer0_.cust_name as cust_nam3_0_ from cst_customer customer0_ where (customer0_.cust_id is not null or customer0_.cust_id is not null) and customer0_.cust_id>? and (customer0_.cust_name in (? , ?))
                System.out.println("\n" + list.equals(repository.findAll(expression)));
            }
            ```

            QueryDSL 对于自定义查询也是支持的:

            先要手动给测试类添加 EntityManager

            ```java
            // 用 Autowired 会有线程安全问题,
            // 要保证线程安全 要用 PersistenceContext
            @PersistenceContext
            private EntityManager em;
            ```

            ```java
            @Test
            public void testEm() {
                JPAQueryFactory factory = new JPAQueryFactory(em);
                QCustomer customer = QCustomer.customer;
                JPAQuery<Tuple> jpaQuery =
                    factory.select(customer.id, customer.name).from(customer).where(customer.name.eq("张三"))
                        .orderBy(customer.id.desc());
                List<Tuple> fetch = jpaQuery.fetch();
                for (Tuple tuple : fetch) {
                    System.out.println(tuple.get(customer.id));
                    System.out.println(tuple.get(customer.name));
                }
            }
            ```

            这里的 `Tuple` 可以理解成一个自定义的对象, 因为在 `select()` 里只查询了两列, 所以对象不是 `Customer`

## multi tables association

Spring Data JPA 没有对 Association 进行实现, 这里用的就是 Hibernate 的实现

Hibernate 的多表关联操作是 Hibernate 最好用的(用注解)... 比 MyBatis MP 写 SQL + request mapping 的方法好

## one to one one-way association

比如:

`tb_customer` 表里: id, cust_name, cust_address, account_id, (id primary key, account_id foreign key)

`tb_account` 表里: aid, password, username, (aid primary key, 以外键形式关联 tb_customer.account_id)

只在一个表里设置另一个表对象, 就是单向关联

比如只在 Customer entity 里设置 Account entity

通过 `JoinColumn` 设置外键字段名

```java
@OneToOne
@JoinColumn(name = "account_id")
private Account account;
```

`@OneToOne(cascade = CascadeType.xxx)` 可能的值:

`ALL`: 所有持久化操作

`PERSIST`: 只有插入才会执行关联操作

`MERGE`: 只有修改才会执行关联操作

`REMOVE`: 只有删除才会执行关联操作

`@OneToOne(fetch = FetchType.xxx)` 可能的值:

`LAZY`,Defines that data can be lazily fetched

`EAGER` Defines that data must be eagerly fetched.

-   一对一单向关联 insert 插入:

    ```java
        @Test
        public void testC() {
            //    初始化数据
            Customer customer = new Customer();
            Account account = new Account();
            account.setUsername("zhangsan");
            account.setPassword("123");
            customer.setCustName("张三");
            customer.setCustAddress("beijing");
            customer.setAccount(account);

            // 没设置关联关系 会报错:
            // object references an unsaved transient instance - save the transient instance before flushing : cn.sichu.pojo.Customer.account -> cn.sichu.pojo.Account
            // 需要设置 @OneToOne() 括号里的内容为: @OneToOne(cascade = CascadeType.PERSIST), PERSIST/ALL 都可以, PERSIST专门对应插入, 跟原生jpa一样
            Customer result = customerRepository.save(customer);
            // Hibernate: insert into tb_account (password, username) values (?, ?)
            // Hibernate: insert into tb_customer (account_id, cust_address, cust_name) values (?, ?, ?)
            // Customer(custId=2, custName=张三, custAddress=beijing, account=Account(id=1, username=zhangsan, password=123))
            System.out.println(result);
        }
    ```

-   一对一单向关联 find 查询:

    可以通过添加 `fetch = xxx` 更改查询模式

    ```java
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    ```

    **但是用延迟加载的时候一定要添加 `@Transactional()` 注解, 虽然之前只有 update 和 delete 的时候才用到**

    ```java
    @Test
    // 更改 FetchType.Lazy 后报错: org.hibernate.LazyInitializationException: could not initialize proxy [cn.sichu.pojo.Account#1] - no Session
    @Transactional(readOnly = true)
    public void testR() {}
    ```

    **为什么要懒加载配置事务?**

    当通过 repository 调用完查询方法, session 就会立即关闭, 一旦 session 关闭就不能再查询和 `toString()` 了

    添加了 `@Transactional(readOnly = true)` 可以让 session 直到事务方法执行完毕后才会关闭

-   一对一单向关联 update 更新:

    ```java
        @Test
        public void testU() {
            // 添加 orphanRemoval = true 前:
            // Hibernate: select customer0_.id as id1_1_1_, customer0_.account_id as account_4_1_1_, customer0_.cust_address as cust_add2_1_1_, customer0_.cust_name as cust_nam3_1_1_, account1_.id as id1_0_0_, account1_.password as password2_0_0_, account1_.username as username3_0_0_ from tb_customer customer0_ left outer join tb_account account1_ on customer0_.account_id=account1_.id where customer0_.id=?
            // Hibernate: update tb_customer set account_id=?, cust_address=?, cust_name=? where id=?
            // Customer(custId=6, custName=李四, custAddress=null, account=null)
            // 添加 orphanRemoval = true 后:
            // Hibernate: select customer0_.id as id1_1_1_, customer0_.account_id as account_4_1_1_, customer0_.cust_address as cust_add2_1_1_, customer0_.cust_name as cust_nam3_1_1_, account1_.id as id1_0_0_, account1_.password as password2_0_0_, account1_.username as username3_0_0_ from tb_customer customer0_ left outer join tb_account account1_ on customer0_.account_id=account1_.id where customer0_.id=?
            // Hibernate: update tb_customer set account_id=?, cust_address=?, cust_name=? where id=?
            // Hibernate: delete from tb_account where id=?
            // Customer(custId=4, custName=李四, custAddress=null, account=null)
            Customer customer = new Customer();
            customer.setCustId(6L);
            customer.setCustName("李四");
            customer.setAccount(null);
            Customer save = customerRepository.save(customer);
            System.out.println(save);
        }
    ```

    ```java
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    ```

    可以通过 `orphanRemoval = true` 设置关联删除, 通常再修改的时候会用到: 一旦把关联的数据设置 null, 或者修改为其他的关联数据, 就可以设置为 true

-   一对一单向关联 其他:

    ```java
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, optional = false)
    ```

    设置 `optional = false` (默认=true), 来限制条件不能为空:

    ```java
    @Test
    public void testU() {
        // 设置 optional = false 后:
        // org.springframework.dao.DataIntegrityViolationException: not-null property references a null or transient value : cn.sichu.pojo.Customer.account; nested exception is org.hibernate.PropertyValueException: not-null property references a null or transient value : cn.sichu.pojo.Customer.account
        Customer customer = new Customer();
        customer.setCustId(7L);
        customer.setCustName("李四");
        customer.setAccount(null);
        Customer save = customerRepository.save(customer);
        System.out.println(save);
    }
    ```

## one to one two-way association

`mappedBy = ""`: 值是 另一方关联的属性名,

比如: 在 Account 实体类中 关联 Customer 表的属性名是 `customer`, 那么在 Customer 实体类中需要取消关联的就是这个值

```java

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;
```

```java
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true,
        optional = false)
    @JoinColumn(name = "account_id")
    private Account account;
```

## one to many and many to one association

一对多要去 Hibernate 官网看文档, 因为 Spring Data JPA 没多表关联实现

实际开发中, 一对多/多对一 出现的场景最多

比如: 用户表-订单表, 用户表视角是一对多, 订单表视角是多对一

一对多默认就是懒加载:

```java
    @Test
    @Transactional(readOnly = true)
    public void testR() {
        Optional<Customer> byId = repository.findById(1L);
        // Hibernate: select customer0_.id as id1_1_0_, customer0_.cust_address as cust_add2_1_0_, customer0_.cust_name as cust_nam3_1_0_, account1_.id as id1_0_1_, account1_.customer_id as customer4_0_1_, account1_.password as password2_0_1_, account1_.username as username3_0_1_ from tb_customer customer0_ left outer join tb_account account1_ on customer0_.id=account1_.customer_id where customer0_.id=?
        // ===============
        // Hibernate: select messages0_.customer_id as customer3_2_0_, messages0_.id as id1_2_0_, messages0_.id as id1_2_1_, messages0_.info as info2_2_1_ from tb_message messages0_ where messages0_.customer_id=?
        // Optional[Customer{custId=1, custName='张三', custAddress='beijing', account=null, messages=[Message{id=1, info='hello'}, Message{id=2, info='world!'}]}]
        // 也就是说 一对多, 默认就是懒加载
        System.out.println("===============");
        System.out.println(byId);
    }
```
