package cn.sichu.mybatis.plus;

import cn.sichu.mybatis.plus.enums.GenderEnum;
import cn.sichu.mybatis.plus.mapper.UserMapper;
import cn.sichu.mybatis.plus.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author sichu
 * @date 2022/12/07
 **/
@SpringBootTest
public class MyBatisPlusEnumTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() {
        User user = new User();
        user.setName("enumTest");
        user.setAge(20);
        user.setGender(GenderEnum.FEMALE);
        System.out.println(userMapper.insert(user));
    }
}
