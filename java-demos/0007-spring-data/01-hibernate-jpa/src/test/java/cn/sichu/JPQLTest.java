package cn.sichu;

import cn.sichu.utils.JPAUtils;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

/**
 * @author sichu
 * @date 2022/12/10
 **/
public class JPQLTest {
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
}
