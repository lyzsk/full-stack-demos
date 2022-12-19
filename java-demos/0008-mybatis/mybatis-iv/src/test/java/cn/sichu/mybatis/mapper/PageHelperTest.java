package cn.sichu.mybatis.mapper;

import cn.sichu.mybatis.pojo.Emp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author sichu
 * @date 2022/12/05
 **/
public class PageHelperTest {

    /**
     * limit关键词 index, pageSize,
     * index: 当前页的起始索引
     * pageSize: 每页显示的条数
     * pageNum: 当前页的页码
     * index = (pageNum - 1) * pageSize
     */

    @Test
    public void testPageHelper() {
        try {
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            SqlSession sqlSession = sqlSessionFactory.openSession(true);
            EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
            PageHelper.startPage(3, 5);
            List<Emp> list = mapper.selectByExample(null);
            list.forEach(System.out::println);
            System.out.println("-------------------");
            PageInfo<Emp> page = new PageInfo<>(list, 3);
            System.out.println(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
