package cn.sichu;

import cn.sichu.config.SpringDataJPAConfig;
import cn.sichu.pojo.Customer;
import cn.sichu.pojo.QCustomer;
import cn.sichu.repositories.CustomerQueryDSLRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author sichu
 * @date 2022/12/10
 **/
@ContextConfiguration(classes = SpringDataJPAConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryDSLTest {
    // 用 Autowired 会有线程安全问题,
    // 要保证线程安全 要用 PersistenceContext
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CustomerQueryDSLRepository repository;

    @Test
    public void testR() {
        QCustomer customer = QCustomer.customer;
        // 通过id查找
        BooleanExpression eq = customer.id.eq(1L);
        Optional<Customer> result = repository.findOne(eq);
        System.out.println(result.get());
    }

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
}
