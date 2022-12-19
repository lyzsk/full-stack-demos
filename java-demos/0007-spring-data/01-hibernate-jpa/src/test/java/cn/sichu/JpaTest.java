package cn.sichu;

import cn.sichu.pojo.Customer;
import cn.sichu.utils.JPAUtils;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author sichu
 * @date 2022/12/10
 **/
public class JpaTest {

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

    @Test
    public void testUpdate() {
        EntityManager em = JPAUtils.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Customer customer = em.find(Customer.class, 3L);
        customer.setCustIndustry("餐饮");
        em.merge(customer);
        tx.commit();
        em.close();
    }

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
}
