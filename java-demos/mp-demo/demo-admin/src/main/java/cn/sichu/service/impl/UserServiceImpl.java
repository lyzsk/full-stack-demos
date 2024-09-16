package cn.sichu.service.impl;

import cn.sichu.constant.Gender;
import cn.sichu.constant.LogicDelete;
import cn.sichu.constant.Result;
import cn.sichu.entity.User;
import cn.sichu.mapper.UserMapper;
import cn.sichu.service.IUserService;
import cn.sichu.utils.DateUtil;
import cn.sichu.utils.NumberUtil;
import cn.sichu.utils.TelNumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author sichu huang
 * @date 2024/09/16
 **/
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public int initUserTable(int count) throws ParseException {
        Faker faker = new Faker();
        while (count > 0) {
            String name = faker.name().lastName() + " " + faker.name().firstName();
            int age = faker.number().numberBetween(18, 31);
            String gender = NumberUtil.isOdd(count) ? Gender.MALE : Gender.FEMALE;
            StringBuilder sb = new StringBuilder();
            sb.append(faker.address().streetAddress(true)).append(" ").append(faker.address().city()).append(" ")
                .append(faker.address().state()).append(" ").append(faker.address().country());
            String address = sb.toString();
            String phoneNumber = TelNumberUtil.getChineseMainLandTelNumber();
            String keySkills = faker.job().keySkills();
            int last = Integer.parseInt(phoneNumber.substring(phoneNumber.length() - 1));
            if (NumberUtil.isOdd(new Random().nextInt(Integer.MAX_VALUE) - last)) {
                keySkills += "," + faker.job().keySkills();
            }
            if (NumberUtil.isPrimeNum(new Random().nextInt(Integer.MAX_VALUE) | last)) {
                keySkills += "," + faker.job().keySkills();
            }
            if (NumberUtil.isHappyNumber(new Random().nextInt(Integer.MAX_VALUE) & age)) {
                keySkills += "," + faker.job().keySkills();
            }
            if (NumberUtil.isUglyNumber(new Random().nextInt(Integer.MAX_VALUE) ^ age)) {
                keySkills += "," + faker.job().keySkills();
            }
            User user = new User(name, gender, age, address, phoneNumber, keySkills);
            user.setCreateBy("admin");
            user.setCreateTime(DateUtil.parseToMills(new Date()));
            user.setDeleted(LogicDelete.NOT_DELETED);
            int result = userMapper.insert(user);
            count -= result;
        }
        return count == 0 ? Result.SUCCESS : Result.FAILED;
    }

    @Override
    public List<User> selectAllUser() {
        return userMapper.selectList(null);
    }

    @Override
    public List<User> selectUserByGenderAndAge(String gender, int min, int max) {
        QueryWrapper<User> qw = new QueryWrapper<User>().eq("gender", gender).ge("age", min).le("age", max);
        return userMapper.selectList(qw);
    }

    @Override
    public List<User> selectUserByConditions(String gender, int minAge, int maxAge, String keySkill) {
        LambdaQueryWrapper<User> lqw =
            new LambdaQueryWrapper<User>().eq(User::getGender, gender).ge(User::getAge, minAge).le(User::getAge, maxAge)
                .like(User::getKeySkills, keySkill).orderByAsc(User::getAge);
        return userMapper.selectList(lqw);
    }
}
