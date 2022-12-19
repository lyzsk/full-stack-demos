package cn.sichu.mybatis.plus;

import cn.sichu.mybatis.plus.mapper.UserMapper;
import cn.sichu.mybatis.plus.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * @author sichu
 * @date 2022/12/05
 **/
@SpringBootTest
public class MyBatisPlusTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectList() {
        // 通过条件构造器(Wrapper) 查询一个List集合, 若没有条件, 则可设置null为参数
        List<User> list = userMapper.selectList(null);
        list.forEach(System.out::println);
    }

    @Test
    public void testInsert() {
        // INSERT INTO user ( id, name, age, email ) VALUES ( ?, ?, ?, ? )
        // res:1
        // id:1599891344171470849
        User user = new User();
        user.setName("uidtest");
        user.setAge(18);
        user.setEmail("uidtest@test.com");
        int res = userMapper.insert(user);
        System.out.println("res:" + res);
        System.out.println("id:" + user.getId());
    }

    @Test
    public void testDelete() {
        // DELETE FROM user WHERE id=?
        // 在id数值后加L转换成Long类型
        // int res = userMapper.deleteById(1599891344171470849L);
        // System.out.println(res);

        // DELETE FROM user WHERE name = ? AND age = ?
        // Map<String, Object> map = new HashMap<>(16);
        // map.put("name", "张三");
        // map.put("age", 23);
        // int res = userMapper.deleteByMap(map);
        // System.out.println(res);

        // DELETE FROM user WHERE id IN ( ? , ? , ? )
        List<Long> list = Arrays.asList(1L, 2L, 3L);
        int res = userMapper.deleteBatchIds(list);
        System.out.println(res);
    }

    @Test
    public void testUpdate() {
        // UPDATE user SET name=?, email=? WHERE id=?
        User user = new User();
        user.setId(4L);
        user.setName("李四");
        user.setEmail("lisi@lisi.com");
        int res = userMapper.updateById(user);
        System.out.println(res);
    }

    @Test
    public void testSelect() {
        // SELECT id,name,age,email FROM user WHERE id=?
        // User user = userMapper.selectById(1L);
        // System.out.println(user);

        // SELECT id,name,age,email FROM user WHERE id IN ( ? , ? )
        // List<Long> list = Arrays.asList(4L, 5L);
        // List<User> users = userMapper.selectBatchIds(list);
        // users.forEach(System.out::println);

        // SELECT id,name,age,email FROM user WHERE name = ? AND age = ?
        // Map<String, Object> map = new HashMap<>(16);
        // map.put("name", "Billie");
        // map.put("age", 24);
        // List<User> users = userMapper.selectByMap(map);
        // users.forEach(System.out::println);

        // SELECT id,name,age,email FROM user
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);

        // select id,name,age,email from user where id=?
        // Map<String, Object> map = userMapper.selectMapById(4L);
        // System.out.println(map);
    }
}
