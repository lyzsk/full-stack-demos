package cn.sichu.mybatis.plus;

import cn.sichu.mybatis.plus.pojo.User;
import cn.sichu.mybatis.plus.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sichu
 * @date 2022/12/05
 **/
@SpringBootTest
public class MyBatisPlusServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testGetCount() {
        // SELECT COUNT( * ) FROM user
        long count = userService.count();
        System.out.println(count);
    }

    @Test
    public void testBatchInsert() {
        // INSERT INTO user ( id, name, age, email ) VALUES ( ?, ?, ?, ? )
        // 本质上是单个sql重复n次, 实现批量添加操作
        List<User> list = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            User user = new User();
            user.setName("test" + i);
            user.setAge(18 + i);
            user.setEmail(user.getName() + "@" + user.getName() + ".com");
            list.add(user);
        }
        boolean res = userService.saveBatch(list);
        System.out.println(res);
    }
}
