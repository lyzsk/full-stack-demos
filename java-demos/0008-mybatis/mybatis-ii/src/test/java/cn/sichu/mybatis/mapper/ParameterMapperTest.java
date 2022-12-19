package cn.sichu.mybatis.mapper;

import cn.sichu.mybatis.pojo.User;
import cn.sichu.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ParameterMapperTest {
    @Test
    public void testCheckLoginByParam() {
        String username = "admin";
        String password = "123456";
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
        System.out.println(mapper.checkLoginByParam(username, password));
    }

    @Test
    public void testInsertUserWithParameter() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
        User user = new User(null, "testAdmin", "999", 24, "女", "testadmin@test.com");
        System.out.println(mapper.insertUserWithParameter(user));
    }

    @Test
    public void testCheckLoginByMap() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
        Map<String, Object> map = new HashMap<>(16);
        map.put("username", "张三");
        map.put("password", "123");
        System.out.println(mapper.checkLoginByMap(map));
    }

    @Test
    public void testGetUserByUsername() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
        System.out.println(mapper.getUserByUsername("李四"));
    }

    @Test
    public void testCheckLoginWithoutParam() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
        System.out.println(mapper.checkLoginWithoutParam("王五", "321"));
    }
}