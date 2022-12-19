package cn.sichu;

import cn.sichu.pojo.Customer;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author sichu
 * @date 2022/12/08
 **/
public class JpaTest {
    private EntityManagerFactory factory;

    @Before
    public void before() {
        factory = Persistence.createEntityManagerFactory("hibernateJPA");
    }

    @Test
    public void testC() {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer customer = new Customer();
        customer.setCustName("王五");
        entityManager.persist(customer);
        transaction.commit();
    }

    @Test
    public void testR() {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer customer = entityManager.find(Customer.class, 2L);
        System.out.println("================");
        System.out.println(customer);
        transaction.commit();
    }

    @Test
    public void testRLazy() {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer customer = entityManager.getReference(Customer.class, 2L);
        System.out.println("==========");
        System.out.println(customer);
        transaction.commit();
    }

    @Test
    public void testU() {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer customer = new Customer();
        // customer.setCustomerId(4L);
        customer.setCustName("王五");
        entityManager.merge(customer);
        transaction.commit();
    }

    @Test
    public void testUJPQL() {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        // Customer customer = new Customer();
        // customer.setCustomerId(5L);
        // customer.setCustName("周八");
        // Hibernate: update cst_customer set cust_name=? where cust_id=?
        String jpql = "UPDATE Customer set custName=:name where custId=:id";
        entityManager.createQuery(jpql).setParameter("name", "吴九").setParameter("id", 5L).executeUpdate();
        transaction.commit();
    }

    @Test
    public void testUSQL() {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        String sql = "UPDATE cst_customer set cust_name=:name where cust_id=:id";
        entityManager.createNativeQuery(sql).setParameter("name", "郑十").setParameter("id", 5L).executeUpdate();
        transaction.commit();
    }

    @Test
    public void testD() {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        // Customer customer = new Customer();
        // customer.setCustomerId(5L);
        // // java.lang.IllegalArgumentException: Removing a detached instance cn.sichu.pojo.Customer#5
        // entityManager.remove(customer);

        // Hibernate: select customer0_.cust_id as cust_id1_0_0_, customer0_.cust_address as cust_add2_0_0_, customer0_.cust_name as cust_nam3_0_0_ from cst_customer customer0_ where customer0_.cust_id=?
        // Hibernate: delete from cst_customer where cust_id=?
        Customer customer = entityManager.find(Customer.class, 5L);
        entityManager.remove(customer);
        transaction.commit();
    }
}
