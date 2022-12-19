package cn.sichu.mybatis.plus;

import cn.sichu.mybatis.plus.mapper.UserMapper;
import cn.sichu.mybatis.plus.pojo.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * @author sichu
 * @date 2022/12/06
 **/
@SpringBootTest
public class MyBatisPlusWrapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test01() {
        // ==>  Preparing: SELECT uid AS id,user_name AS name,age,email,is_deleted FROM t_user WHERE is_deleted=0 AND (user_name LIKE ? AND age BETWEEN ? AND ? AND email IS NOT NULL)
        // ==> Parameters: %a%(String), 20(Integer), 30(Integer)
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name", "a").between("age", 20, 30).isNotNull("email");
        List<User> list = userMapper.selectList(queryWrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void test02() {
        // ==>  Preparing: SELECT uid AS id,user_name AS name,age,email,is_deleted FROM t_user WHERE is_deleted=0 ORDER BY age DESC,uid ASC
        // ==> Parameters:
        // <==    Columns: id, name, age, email, is_deleted
        // <==        Row: 5, Billie, 24, test5@baomidou.com, 0
        // <==        Row: 4, Sandy, 21, test4@baomidou.com, 0
        // <==        Row: 6, uidtest, 18, uidtest@test.com, 0
        // <==        Row: 7, uidtest, 18, uidtest@test.com, 0
        // <==        Row: 8, uidtest, 18, uidtest@test.com, 0
        // <==        Row: 9, uidtest, 18, uidtest@test.com, 0
        // <==        Row: 10, uidtest, 18, uidtest@test.com, 0
        // <==      Total: 7
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("age").orderByAsc("uid");
        List<User> list = userMapper.selectList(queryWrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void test03() {
        // 因为添加了逻辑删除, 所以此时的删除操作变成了update操作
        // ==>  Preparing: UPDATE t_user SET is_deleted=1 WHERE is_deleted=0 AND (email IS NULL)
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("email");
        int res = userMapper.delete(queryWrapper);
        System.out.println(res);
    }

    @Test
    public void test04() {
        // ==>  Preparing: UPDATE t_user SET user_name=?, email=? WHERE is_deleted=0 AND (age > ? AND user_name LIKE ? OR email IS NULL)
        // ==> Parameters: 小明(String), xiaoming@xiaoming.com(String), 20(Integer), %a%(String)
        // <==    Updates: 1
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("age", 20).like("user_name", "a").or().isNull("email");
        User user = new User();
        user.setName("小明");
        user.setEmail("xiaoming@xiaoming.com");
        int res = userMapper.update(user, queryWrapper);
        System.out.println(res);
    }

    @Test
    public void test05() {
        // ==>  Preparing: UPDATE t_user SET user_name=?, email=? WHERE is_deleted=0 AND (user_name LIKE ? AND (age > ? OR email IS NULL))
        // ==> Parameters: xiaohong(String), xiaohong@xiaohong.com(String), %a%(String), 20(Integer)
        // <==    Updates: 1
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name", "a").and(i -> i.gt("age", 20).or().isNull("email"));
        User user = new User();
        user.setName("xiaohong");
        user.setEmail("xiaohong@xiaohong.com");
        int res = userMapper.update(user, queryWrapper);
        System.out.println(res);
    }

    @Test
    public void test06() {
        // ==>  Preparing: SELECT user_name,age,email FROM t_user WHERE is_deleted=0
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("user_name", "age", "email");
        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
        maps.forEach(System.out::println);
    }

    @Test
    public void test07() {
        // ==>  Preparing: SELECT uid AS id,user_name AS name,age,email,is_deleted FROM t_user WHERE is_deleted=0 AND (uid IN (select uid from t_user where uid<=100))
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("uid", "select uid from t_user where uid<=100");
        List<User> list = userMapper.selectList(queryWrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void test08() {
        // ==>  Preparing: UPDATE t_user SET user_name=?,email=? WHERE is_deleted=0 AND (user_name LIKE ? AND (age > ? OR email IS NULL))
        // ==> Parameters: xiaohei(String), xiaohei@xiaohei.com(String), %a%(String), 20(Integer)
        // 这样就可以给Entity传null, 不用手动生成实体类对象了
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.like("user_name", "a").and(i -> i.gt("age", 20).or().isNull("email"));
        updateWrapper.set("user_name", "xiaohei").set("email", "xiaohei@xiaohei.com");
        int res = userMapper.update(null, updateWrapper);
        System.out.println(res);
    }

    @Test
    public void test09() {
        // ==>  Preparing: SELECT uid AS id,user_name AS name,age,email,is_deleted FROM t_user WHERE is_deleted=0 AND (age >= ? AND age <= ?)
        // ==> Parameters: 20(Integer), 30(Integer)

        // ==>  Preparing: SELECT uid AS id,user_name AS name,age,email,is_deleted FROM t_user WHERE is_deleted=0 AND (user_name LIKE ? AND age <= ?)
        // ==> Parameters: %a%(String), 30(Integer)
        String username = "a";
        Integer ageBegin = null;
        Integer ageEnd = 30;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("user_name", username);
        }
        if (ageBegin != null) {
            queryWrapper.ge("age", ageBegin);
        }
        if (ageEnd != null) {
            queryWrapper.le("age", ageEnd);
        }
        List<User> list = userMapper.selectList(queryWrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void test10() {
        // ==>  Preparing: SELECT uid AS id,user_name AS name,age,email,is_deleted FROM t_user WHERE is_deleted=0 AND (user_name LIKE ? AND age <= ?)
        // ==> Parameters: %a%(String), 30(Integer)
        // 也就是test09的简便方法, 用 condition 参数动态组装
        String username = "a";
        Integer ageBegin = null;
        Integer ageEnd = 30;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username), "user_name", username).ge(ageBegin != null, "age", ageBegin)
            .le(ageEnd != null, "age", ageEnd);
        List<User> list = userMapper.selectList(queryWrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void test11() {
        // ==>  Preparing: SELECT uid AS id,user_name AS name,age,email,is_deleted FROM t_user WHERE is_deleted=0 AND (user_name LIKE ? AND age <= ?)
        // ==> Parameters: %a%(String), 30(Integer)
        // 跟09, 10一样的sql语句, 用lambda表达式防止字段名写错, 就不用查表头了
        String username = "a";
        Integer ageBegin = null;
        Integer ageEnd = 30;
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotBlank(username), User::getName, username)
            .ge(ageBegin != null, User::getAge, ageBegin).le(ageEnd != null, User::getAge, ageEnd);
        List<User> list = userMapper.selectList(lambdaQueryWrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void test12() {
        // ==>  Preparing: UPDATE t_user SET user_name=?,email=? WHERE is_deleted=0 AND (user_name LIKE ? AND (age > ? OR email IS NULL))
        // ==> Parameters: xiaobai(String), xiaobai@test.com(String), %a%(String), 20(Integer)
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.like(User::getName, "a").and(i -> i.gt(User::getAge, 20).or().isNull(User::getEmail));
        updateWrapper.set(User::getName, "xiaobai").set(User::getEmail, "xiaobai@test.com");
        int res = userMapper.update(null, updateWrapper);
        System.out.println(res);
    }
}
