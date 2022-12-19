# JPA

1. 导入依赖

```xml
    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <build.sourceEncoding>UTF-8</build.sourceEncoding>
        <hibernate.version>5.0.7.Final</hibernate.version>
    </properties>

    <dependencies>
        <!-- junit -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>

        <!-- hibernate对jpa的支持包 -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>${hibernate.version}</version>
        </dependency>

        <!-- c3p0 -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-c3p0</artifactId>
            <version>${hibernate.version}</version>
        </dependency>

        <!-- log日志 -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.12.1</version>
        </dependency>


        <!-- Mysql and MariaDB -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.28</version>
        </dependency>

        <!--lombok-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.12</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
```

2. 创建表 (因为这里没用 hibernate session 来关联所以不会自动创建表且会报错)

```sql
 /*创建客户表*/
    CREATE TABLE cst_customer (
      cust_id bigint(32) NOT NULL AUTO_INCREMENT COMMENT '客户编号(主键)',
      cust_name varchar(32) NOT NULL COMMENT '客户名称(公司名称)',
      cust_source varchar(32) DEFAULT NULL COMMENT '客户信息来源',
      cust_industry varchar(32) DEFAULT NULL COMMENT '客户所属行业',
      cust_level varchar(32) DEFAULT NULL COMMENT '客户级别',
      cust_address varchar(128) DEFAULT NULL COMMENT '客户联系地址',
      cust_phone varchar(64) DEFAULT NULL COMMENT '客户联系电话',
      PRIMARY KEY (`cust_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

3. 创建实体类

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cst_customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_id")
    private Long custId;

    @Column(name = "cust_name")
    private String custName;

    @Column(name = "cust_source")
    private String custSource;

    @Column(name = "cust_level")
    private String custLevel;

    @Column(name = "cust_industry")
    private String custIndustry;

    @Column(name = "cust_phone")
    private String custPhone;

    @Column(name = "cust_address")
    private String custAddress;
}
```

4. 创建 `resources/META-INF/persistence.xml`, 并配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://java.sun.com/xml/ns/persistence" version="2.0">
    <!--需要配置persistence-unit节点
        持久化单元：
            name:持久化单元名称
            transaction-type:事务管理的方式
                JTA:分布式事务管理
                RESOURCE_LOCAL:本地事务管理
    -->
    <persistence-unit name="myJpa" transaction-type="RESOURCE_LOCAL">
        <!--jpa的实现方式-->
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        <!--可选配置：配置jpa实现方式的配置信息-->
        <properties>
            <!-- 数据库信息
                用户名，javax.persistence.jdbc.user
                密码，  javax.persistence.jdbc.password
                驱动，  javax.persistence.jdbc.driver
                数据库地址   javax.persistence.jdbc.url
            -->
            <property name="javax.persistence.jdbc.user" value="root"/>
            <property name="javax.persistence.jdbc.password" value="root"/>
            <property name="javax.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
            <property name="javax.persistence.jdbc.url"
                      value="jdbc:mysql://localhost:3306/springdata_jpa_new?characterEncoding=utf-8&amp;useSSL=false&amp;useUnicode=true&amp;serverTimezone=UTC"/>

            <!--配置jpa实现方(hibernate)的配置信息
                显示sql           ：   false|true
                自动创建数据库表    ：  hibernate.hbm2ddl.auto
                        create      : 程序运行时创建数据库表（如果有表，先删除表再创建）
                        update      ：程序运行时创建表（如果有表，不会创建表）
                        none        ：不会创建表
            -->
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
        </properties>
    </persistence-unit>

</persistence>

```

## test 1

测试

```java
    @Test
    public void testSave() {

        // 1. 加载配置文件创建工厂对象
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("myJpa");
        // 2. 通过实体管理工厂获取实体管理器
        EntityManager em = factory.createEntityManager();
        // 3. 获取事务对象
        EntityTransaction tx = em.getTransaction();
        // 开启事务
        tx.begin();
        // 4. 创建对象
        Customer customer = new Customer();
        customer.setCustName("张三");
        customer.setCustIndustry("教育");
        // 保存
        em.persist(customer);
        // 5. 提交事务
        tx.commit();
        // 6. 释放资源
        em.close();
        factory.close();
    }
```

output:

```console
Hibernate: insert into cst_customer (cust_address, cust_industry, cust_level, cust_name, cust_phone, cust_source) values (?, ?, ?, ?, ?, ?)
```

## test 2

1. 创建工具类 用于通过创建工厂对象生成实体管理器

```java
public final class JPAUtils {
    // JPA的实体管理器工厂：相当于Hibernate的SessionFactory
    private static EntityManagerFactory factory;

    // 使用静态代码块赋值
    static {
        // 注意：该方法参数必须和persistence.xml中persistence-unit标签name属性取值一致
        factory = Persistence.createEntityManagerFactory("myJpa");
    }

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }
}
```

2. 测试

-   测试 create

```java
    @Test
    public void testSave2() {
        EntityManager em = JPAUtils.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Customer customer = new Customer();
        customer.setCustName("李四");
        em.persist(customer);
        tx.commit();
        em.close();
    }
```

output:

```
Hibernate: insert into cst_customer (cust_address, cust_industry, cust_level, cust_name, cust_phone, cust_source) values (?, ?, ?, ?, ?, ?)
```

-   测试 update

```java
    @Test
    public void testUpdate() {
        EntityManager em = JPAUtils.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Customer customer = em.find(Customer.class, 2L);
        customer.setCustIndustry("餐饮");
        em.merge(customer);
        tx.commit();
        em.close();
    }
```

output:

```
Hibernate: select customer0_.cust_id as cust_id1_0_0_, customer0_.cust_address as cust_add2_0_0_, customer0_.cust_industry as cust_ind3_0_0_, customer0_.cust_level as cust_lev4_0_0_, customer0_.cust_name as cust_nam5_0_0_, customer0_.cust_phone as cust_pho6_0_0_, customer0_.cust_source as cust_sou7_0_0_ from cst_customer customer0_ where customer0_.cust_id=?
Hibernate: update cst_customer set cust_address=?, cust_industry=?, cust_level=?, cust_name=?, cust_phone=?, cust_source=? where cust_id=?
```

-   测试 delete

```java
    @Test
    public void testDelete() {
        EntityManager em = JPAUtils.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Customer customer = em.find(Customer.class, 2L);
        em.remove(customer);
        tx.commit();
        em.close();
    }
```

output:

```
Hibernate: select customer0_.cust_id as cust_id1_0_0_, customer0_.cust_address as cust_add2_0_0_, customer0_.cust_industry as cust_ind3_0_0_, customer0_.cust_level as cust_lev4_0_0_, customer0_.cust_name as cust_nam5_0_0_, customer0_.cust_phone as cust_pho6_0_0_, customer0_.cust_source as cust_sou7_0_0_ from cst_customer customer0_ where customer0_.cust_id=?
Hibernate: delete from cst_customer where cust_id=?
```

-   测试 read

```java
    @Test
    public void testFind() {
        EntityManager em = JPAUtils.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Customer customer = em.find(Customer.class, 1L);
        System.out.println(customer);
        tx.commit();
        em.close();
    }
```

output:

```
Hibernate: select customer0_.cust_id as cust_id1_0_0_, customer0_.cust_address as cust_add2_0_0_, customer0_.cust_industry as cust_ind3_0_0_, customer0_.cust_level as cust_lev4_0_0_, customer0_.cust_name as cust_nam5_0_0_, customer0_.cust_phone as cust_pho6_0_0_, customer0_.cust_source as cust_sou7_0_0_ from cst_customer customer0_ where customer0_.cust_id=?
Customer(custId=1, custName=张三, custSource=null, custLevel=null, custIndustry=教育, custPhone=null, custAddress=null)
```

延迟加载:

```java
    @Test
    public void testFindLazy() {
        EntityManager em = JPAUtils.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Customer customer = em.getReference(Customer.class, 1L);
        System.out.println("==============");
        System.out.println(customer);
        tx.commit();
        em.close();
    }
```

output:

```
==============
Hibernate: select customer0_.cust_id as cust_id1_0_0_, customer0_.cust_address as cust_add2_0_0_, customer0_.cust_industry as cust_ind3_0_0_, customer0_.cust_level as cust_lev4_0_0_, customer0_.cust_name as cust_nam5_0_0_, customer0_.cust_phone as cust_pho6_0_0_, customer0_.cust_source as cust_sou7_0_0_ from cst_customer customer0_ where customer0_.cust_id=?
Customer(custId=1, custName=张三, custSource=null, custLevel=null, custIndustry=教育, custPhone=null, custAddress=null)
```

## test 3

复杂查询, 用 JPQL:

-   test findall:

```java
    /**
     * 查询全部
     * jqpl：from cn.itcast.domain.Customer
     * sql：SELECT * FROM cst_customer
     */
    @Test
    public void testFindAll() {
        EntityManager em = JPAUtils.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        String jpql = "from Customer";
        // 创建Query查询对象，query对象才是执行jqpl的对象
        Query query = em.createQuery(jpql);
        // 发送查询，并封装结果集
        List list = query.getResultList();
        for (Object obj : list) {
            System.out.println(obj);
        }
        tx.commit();
        em.close();
    }
```

output:

```
Hibernate: select customer0_.cust_id as cust_id1_0_, customer0_.cust_address as cust_add2_0_, customer0_.cust_industry as cust_ind3_0_, customer0_.cust_level as cust_lev4_0_, customer0_.cust_name as cust_nam5_0_, customer0_.cust_phone as cust_pho6_0_, customer0_.cust_source as cust_sou7_0_ from cst_customer customer0_
Customer(custId=1, custName=张三, custSource=null, custLevel=null, custIndustry=教育, custPhone=null, custAddress=null)

```

-   test order by

```java
    /**
     * 排序查询： 倒序查询全部客户
     * sql：SELECT * FROM cst_customer ORDER BY cust_id DESC
     * jpql：from Customer order by custId desc
     */
    @Test
    public void testOrderBy() {
        EntityManager em = JPAUtils.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        String jpql = "from Customer order by custId desc";
        Query query = em.createQuery(jpql);
        List list = query.getResultList();
        for (Object obj : list) {
            System.out.println(obj);
        }
        tx.commit();
        em.close();
    }
```

output:

```
Hibernate: select customer0_.cust_id as cust_id1_0_, customer0_.cust_address as cust_add2_0_, customer0_.cust_industry as cust_ind3_0_, customer0_.cust_level as cust_lev4_0_, customer0_.cust_name as cust_nam5_0_, customer0_.cust_phone as cust_pho6_0_, customer0_.cust_source as cust_sou7_0_ from cst_customer customer0_ order by customer0_.cust_id desc
Customer(custId=3, custName=李四, custSource=null, custLevel=null, custIndustry=餐饮, custPhone=null, custAddress=null)
Customer(custId=1, custName=张三, custSource=null, custLevel=null, custIndustry=教育, custPhone=null, custAddress=null)

```

-   test count

```java
    /**
     * 使用jpql查询，统计客户的总数
     * sql：SELECT COUNT(cust_id) FROM cst_customer
     * jpql：select count(custId) from Customer
     */
    @Test
    public void testCount() {
        EntityManager em = JPAUtils.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        String jpql = "select count(custId) from Customer";
        Query query = em.createQuery(jpql);
        List resultList = query.getResultList();
        for (Object obj : resultList) {
            System.out.println(obj);
        }
        tx.commit();
        em.close();
    }
```

output:

```
Hibernate: select count(customer0_.cust_id) as col_0_0_ from cst_customer customer0_
2
```

-   test paging

```java
    /**
     * 分页查询
     * sql：select * from cst_customer limit 0,1
     * jqpl : from Customer
     */
    @Test
    public void testPaging() {
        EntityManager em = JPAUtils.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        String jpql = "from Customer";
        em.createQuery(jpql);
        Query query = em.createQuery(jpql);
        // 起始索引
        query.setFirstResult(0);
        // 每页查询的条数
        query.setMaxResults(1);
        List resultList = query.getResultList();
        for (Object obj : resultList) {
            System.out.println(obj);
        }
        tx.commit();
        em.close();
    }
```

output:

```
Hibernate: select customer0_.cust_id as cust_id1_0_, customer0_.cust_address as cust_add2_0_, customer0_.cust_industry as cust_ind3_0_, customer0_.cust_level as cust_lev4_0_, customer0_.cust_name as cust_nam5_0_, customer0_.cust_phone as cust_pho6_0_, customer0_.cust_source as cust_sou7_0_ from cst_customer customer0_ limit ?
Customer(custId=1, custName=张三, custSource=null, custLevel=null, custIndustry=教育, custPhone=null, custAddress=null)

```

-   test condition query

```java
    /**
     * 条件查询
     * sql：SELECT * FROM cst_customer WHERE cust_name LIKE  ?
     * jpql: from Customer where custName like ?
     */
    @Test
    public void testConditionQuery() {
        EntityManager em = JPAUtils.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        String jpql = "from Customer where custName like ?";
        Query query = em.createQuery(jpql);
        // position: 占位符从 1 开始, value: 具体取值
        query.setParameter(1, "张三");
        List resultList = query.getResultList();
        for (Object obj : resultList) {
            System.out.println(obj);
        }
        tx.commit();
        em.close();
    }
```

output:

```
Hibernate: select customer0_.cust_id as cust_id1_0_, customer0_.cust_address as cust_add2_0_, customer0_.cust_industry as cust_ind3_0_, customer0_.cust_level as cust_lev4_0_, customer0_.cust_name as cust_nam5_0_, customer0_.cust_phone as cust_pho6_0_, customer0_.cust_source as cust_sou7_0_ from cst_customer customer0_ where customer0_.cust_name like ?
Customer(custId=1, custName=张三, custSource=null, custLevel=null, custIndustry=教育, custPhone=null, custAddress=null)

```

# spring data jpa

one to one, one to many, many to one 的时候, 主表的实体类的 toString() 一定不能包含对象表的实体类!!!! 否则会 stackOverFlow() !!!
