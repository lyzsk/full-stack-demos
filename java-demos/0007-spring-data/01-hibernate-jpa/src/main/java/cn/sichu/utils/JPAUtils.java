package cn.sichu.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author sichu
 * @date 2022/12/10
 **/
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
