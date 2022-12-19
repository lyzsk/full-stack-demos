package cn.sichu.mybatis.mapper;

import cn.sichu.mybatis.pojo.Emp;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class EmpMapperTest {
    @Test
    public void selectByExample() {
        try {
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            SqlSession sqlSession = sqlSessionFactory.openSession(true);
            EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
            //  查询所有数据
            // List<Emp> list = mapper.selectByExample(null);
            // list.forEach(System.out::println);
            //  根据条件查询
            // EmpExample empExample = new EmpExample();
            // empExample.createCriteria().andEmpNameEqualTo("张三").andAgeGreaterThanOrEqualTo(18);
            // empExample.or().andDidIsNotNull();
            // List<Emp> list = mapper.selectByExample(empExample);
            // list.forEach(System.out::println);
            // int res = mapper.updateByPrimaryKey(new Emp(9, "admin", 20, null, "admin@outlook.com", 1));
            // System.out.println(res);
            //    此时手动把数据库里 null 的性别改成 女, 因为方法里有null是不会修改数据库里的字段的
            int res = mapper.updateByPrimaryKeySelective(new Emp(9, "admin", 20, null, "admin@outlook.com", 1));
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}