package cn.sichu.mybatis.test;

import cn.sichu.mybatis.mapper.CacheMapper;
import cn.sichu.mybatis.pojo.Emp;
import cn.sichu.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author sichu
 * @date 2022/12/04
 **/
public class CacheMapperTest {

    @Test
    public void testCacheSameMapper() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        CacheMapper mapper = sqlSession.getMapper(CacheMapper.class);
        // 从同一个mapper里取两次相同值的数据
        // 只会执行一次sql, 因为第二次是从cache里取的
        // DEBUG 12-04 19:16:54,780 ==>  Preparing: select eid, emp_name, age, gender, email from t_emp where eid = ? (BaseJdbcLogger.java:137)
        // DEBUG 12-04 19:16:54,877 ==> Parameters: 1(Integer) (BaseJdbcLogger.java:137)
        // DEBUG 12-04 19:16:55,217 <==      Total: 1 (BaseJdbcLogger.java:137)
        // Emp{eid=1, empName='张三', age=18, gender='男', email='zhangsan@qq.com', dept=null}
        // Emp{eid=1, empName='张三', age=18, gender='男', email='zhangsan@qq.com', dept=null}
        Emp emp1 = mapper.getEmpByEid(1);
        System.out.println(emp1);
        Emp emp2 = mapper.getEmpByEid(1);
        System.out.println(emp2);
    }

    @Test
    public void testCacheDiffMapper() {
        // 哪怕用不同的mapper, 因为 L1 cache 是 sqlsession 级别的
        // 同样是只执行一次sql
        // DEBUG 12-04 19:21:42,124 ==>  Preparing: select eid, emp_name, age, gender, email from t_emp where eid = ? (BaseJdbcLogger.java:137)
        // DEBUG 12-04 19:21:42,172 ==> Parameters: 1(Integer) (BaseJdbcLogger.java:137)
        // DEBUG 12-04 19:21:42,224 <==      Total: 1 (BaseJdbcLogger.java:137)
        // Emp{eid=1, empName='张三', age=18, gender='男', email='zhangsan@qq.com', dept=null}
        // Emp{eid=1, empName='张三', age=18, gender='男', email='zhangsan@qq.com', dept=null}
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        CacheMapper mapper1 = sqlSession.getMapper(CacheMapper.class);
        Emp emp1 = mapper1.getEmpByEid(1);
        System.out.println(emp1);
        CacheMapper mapper2 = sqlSession.getMapper(CacheMapper.class);
        Emp emp2 = mapper2.getEmpByEid(1);
        System.out.println(emp2);
    }

    @Test
    public void testCacheDiffSqlSession() {
        // 使用不同的 SqlSession 对象就能执行两次 sql, 因为第一次查询到的信息存到的是第一次的 SqlSession 级别的 L1 Cache 中
        // DEBUG 12-04 19:24:51,565 ==>  Preparing: select eid, emp_name, age, gender, email from t_emp where eid = ? (BaseJdbcLogger.java:137)
        // DEBUG 12-04 19:24:51,614 ==> Parameters: 1(Integer) (BaseJdbcLogger.java:137)
        // DEBUG 12-04 19:24:51,656 <==      Total: 1 (BaseJdbcLogger.java:137)
        // Emp{eid=1, empName='张三', age=18, gender='男', email='zhangsan@qq.com', dept=null}
        // DEBUG 12-04 19:24:51,764 ==>  Preparing: select eid, emp_name, age, gender, email from t_emp where eid = ? (BaseJdbcLogger.java:137)
        // DEBUG 12-04 19:24:51,765 ==> Parameters: 1(Integer) (BaseJdbcLogger.java:137)
        // DEBUG 12-04 19:24:51,771 <==      Total: 1 (BaseJdbcLogger.java:137)
        // Emp{eid=1, empName='张三', age=18, gender='男', email='zhangsan@qq.com', dept=null}

        SqlSession sqlSession1 = SqlSessionUtil.getSqlSession();
        CacheMapper mapper1 = sqlSession1.getMapper(CacheMapper.class);
        System.out.println(mapper1.getEmpByEid(1));
        SqlSession sqlSession2 = SqlSessionUtil.getSqlSession();
        CacheMapper mapper2 = sqlSession2.getMapper(CacheMapper.class);
        System.out.println(mapper2.getEmpByEid(1));
    }

    @Test
    public void testCacheDisableLevelOne() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        CacheMapper mapper = sqlSession.getMapper(CacheMapper.class);
        Emp emp1 = mapper.getEmpByEid(1);
        System.out.println(emp1);
        // mapper.insertEmp(new Emp(null, "测试", 25, "女", "ceshi@qq.com", null));
        sqlSession.clearCache();
        Emp emp2 = mapper.getEmpByEid(1);
        System.out.println(emp2);
    }

    @Test
    public void testCacheEnableLevelTwo() {
        // 不能用工具类写的了, 因为每次调用 try就创建了一个新的 SqlSessionFactory
        try {
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            SqlSession sqlSession1 = sqlSessionFactory.openSession(true);
            CacheMapper mapper1 = sqlSession1.getMapper(CacheMapper.class);
            System.out.println(mapper1.getEmpByEid(1));
            sqlSession1.close();
            SqlSession sqlSession2 = sqlSessionFactory.openSession(true);
            CacheMapper mapper2 = sqlSession2.getMapper(CacheMapper.class);
            System.out.println(mapper2.getEmpByEid(1));
            sqlSession2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
