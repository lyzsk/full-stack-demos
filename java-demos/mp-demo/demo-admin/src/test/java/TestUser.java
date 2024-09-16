import cn.sichu.constant.Gender;
import cn.sichu.constant.KeySkills;
import cn.sichu.entity.User;
import cn.sichu.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.List;

/**
 * @author sichu huang
 * @date 2024/09/16
 **/
@SpringBootTest(classes = Application.class)
public class TestUser {
    @Autowired
    IUserService userService;

    @Test
    public void initUserTable() throws ParseException {
        int result = userService.initUserTable(100);
        System.err.println(result);
    }

    @Test
    public void selectAllUser() {
        List<User> users = userService.selectAllUser();
        for (User user : users) {
            System.err.println(user.toString());
        }
    }

    @Test
    public void selectUserByGenderAndAge() {
        List<User> users = userService.selectUserByGenderAndAge(Gender.FEMALE, 18, 25);
        for (User user : users) {
            System.err.println(user.toString());
        }
        System.out.println(users.size());
    }

    @Test
    public void selectUserByConditions() {
        List<User> users = userService.selectUserByConditions(Gender.FEMALE, 20, 25, KeySkills.COMMUNICATION);
        for (User user : users) {
            System.err.println(user.toString());
        }
        System.out.println(users.size());
    }
}
