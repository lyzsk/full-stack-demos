package cn.sichu.mybatis.test;

import cn.sichu.mybatis.mapper.DynamicSqlMapper;
import cn.sichu.mybatis.pojo.Emp;
import cn.sichu.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author sichu
 * @date 2022/12/04
 **/
public class DynamicSqlMapperTest {
    @Test
    public void testGetEmpByConditions() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        DynamicSqlMapper mapper = sqlSession.getMapper(DynamicSqlMapper.class);
        // List<Emp> list = mapper.getEmpByConditions(new Emp(null, "张三", 18, "男", "zhangsan@qq.com"));
        List<Emp> list = mapper.getEmpByConditions(new Emp(null, "李四", 18, "男", "zhangsan@qq.com"));
        // List<Emp> list = mapper.getEmpByConditions(new Emp(null, "张三", null, "男", "zhangsan@qq.com"));
        // List<Emp> list = mapper.getEmpByConditions(new Emp(null, "", null, "", ""));
        System.out.println(list);
    }

    @Test
    public void testGetEmpByChoose() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        DynamicSqlMapper mapper = sqlSession.getMapper(DynamicSqlMapper.class);
        // List<Emp> list = mapper.getEmpByChoose(new Emp(null, "张三", 18, "男", "zhangsan@qq.com"));
        // List<Emp> list = mapper.getEmpByChoose(new Emp(null, "", 18, "男", "zhangsan@qq.com"));
        List<Emp> list = mapper.getEmpByChoose(new Emp(null, "", null, "", ""));
        System.out.println(list);
    }

    @Test
    public void testDeleteMultiByArray() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        DynamicSqlMapper mapper = sqlSession.getMapper(DynamicSqlMapper.class);
        int res = mapper.deleteMultiByArray(new Integer[] {6, 7, 8});
        System.out.println(res);
    }

    @Test
    public void testInsertMultiByList() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        DynamicSqlMapper mapper = sqlSession.getMapper(DynamicSqlMapper.class);
        Emp emp1 = new Emp(null, "test1", 23, "男", "test1@outlook.com");
        Emp emp2 = new Emp(null, "test2", 24, "女", "test2@qq.com");
        Emp emp3 = new Emp(null, "test3", 25, "女", "test3@gmail.com");
        List<Emp> list = Arrays.asList(emp1, emp2, emp3);
        int res = mapper.insertMultiByList(list);
        System.out.println(res);
    }
}
