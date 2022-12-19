package cn.sichu;

import cn.sichu.pojo.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author sichu
 * @date 2022/12/08
 **/
public class HibernateTest {

    // 类似 MyBatis 的 SqlSessionFactory
    private SessionFactory sf;

    @Before
    public void init() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("/hibernate.cfg.xml").build();
        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    @Test
    public void testC() {
        // 通过 session 进行持久化操作
        // 使用 try 这样可以帮助我们自动关闭session
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            Customer customer = new Customer();
            customer.setCustName("张三");
            session.save(customer);
            transaction.commit();
        }
    }

    @Test
    public void testR() {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            Customer customer = session.find(Customer.class, 1L);
            // 普通查询, 什么时候find()什么时候查询
            // Hibernate:
            //     select
            //         customer0_.cust_id as cust_id1_0_0_,
            //         customer0_.cust_address as cust_add2_0_0_,
            //         customer0_.cust_name as cust_nam3_0_0_
            //     from
            //         cst_customer customer0_
            //     where
            //         customer0_.cust_id=?
            // ==============
            // Customer{customerId=1, custName='张三', custAddress='null'}
            //
            // Process finished with exit code 0
            System.out.println("==============");
            System.out.println(customer);
            transaction.commit();
        }
    }

    @Test
    public void testRLazy() {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            Customer customer = session.load(Customer.class, 1L);
            System.out.println("===============");
            // lazy loading, 直到用到customer对象了才开始查询
            // ===============
            // Hibernate:
            // select
            // customer0_.cust_id as cust_id1_0_0_,
            //     customer0_.cust_address as cust_add2_0_0_,
            // customer0_.cust_name as cust_nam3_0_0_
            //     from
            // cst_customer customer0_
            // where
            // customer0_.cust_id=?
            // Customer{customerId=1, custName='张三', custAddress='null'}
            //
            // Process finished with exit code 0
            System.out.println(customer);
            transaction.commit();
        }
    }

    @Test
    public void TestU() {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            // Customer customer = new Customer();
            // customer.setCustName("李四");
            // customer.setCustAddress("lisi-address");
            // // Hibernate:
            // //     insert
            // //     into
            // //         cst_customer
            // //         (cust_address, cust_name)
            // //     values
            // //         (?, ?)
            // // Customer{customerId=2, custName='李四', custAddress='lisi-address'}
            // session.save("CustName", customer);
            // System.out.println(customer);

            Customer customer1 = new Customer();
            // Hibernate:
            //     update
            //         cst_customer
            //     set
            //         cust_address=?,
            //         cust_name=?
            //     where
            //         cust_id=?
            customer1.setCustomerId(2L);
            customer1.setCustName("李四-new");
            customer1.setCustAddress("lisi-address-new");
            session.saveOrUpdate(customer1);
            transaction.commit();
        }
    }

    @Test
    public void testD() {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            Customer customer = new Customer();
            customer.setCustomerId(3L);
            // Hibernate:
            //     delete
            //     from
            //         cst_customer
            //     where
            //         cust_id=?
            session.remove(customer);
            transaction.commit();
        }
    }

    @Test
    public void testRJPQLHQL() {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            // Hibernate:
            //     select
            //         customer0_.cust_id as cust_id1_0_,
            //         customer0_.cust_address as cust_add2_0_,
            //         customer0_.cust_name as cust_nam3_0_
            //     from
            //         cst_customer customer0_
            //
            // FROM后写的不是sql中的表名, 而是实体类中的表所对应的类名
            //
            // Hibernate:
            //     select
            //         customer0_.cust_id as cust_id1_0_,
            //         customer0_.cust_address as cust_add2_0_,
            //         customer0_.cust_name as cust_nam3_0_
            //     from
            //         cst_customer customer0_
            //     where
            //         customer0_.cust_id=?
            // [Customer{custId=2, custName='李四-new', custAddress='lisi-address-new'}]
            String hql = "FROM Customer where custId=:id";
            List<Customer> resultList = session.createQuery(hql, Customer.class).setParameter("id", 2L).getResultList();
            System.out.println(resultList);
            transaction.commit();
        }
    }
}
