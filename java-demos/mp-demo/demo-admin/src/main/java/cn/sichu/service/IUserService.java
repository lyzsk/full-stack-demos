package cn.sichu.service;

import cn.sichu.entity.User;

import java.text.ParseException;
import java.util.List;

/**
 * @author sichu huang
 * @date 2024/09/16
 **/
public interface IUserService {

    /**
     * @param count 初始化插入条数
     * @return int cn.sichu.constant.Result
     * @author sichu huang
     * @date 2024/09/16
     **/
    int initUserTable(int count) throws ParseException;

    /**
     * 查询`t_user`所有数据
     *
     * @return java.util.List<cn.sichu.entity.User>
     * @author sichu huang
     * @date 2024/09/16
     **/
    List<User> selectAllUser();

    /**
     * @param gender gender
     * @param min    inclusive
     * @param max    inclusive
     * @return java.util.List<cn.sichu.entity.User>
     * @author sichu huang
     * @date 2024/09/16
     **/
    List<User> selectUserByGenderAndAge(String gender, int min, int max);

    /**
     * @param gender   gender
     * @param minAge   minAge
     * @param maxAge   maxAge
     * @param keySkill keySkill
     * @return java.util.List<cn.sichu.entity.User>
     * @author sichu huang
     * @date 2024/09/16
     **/
    List<User> selectUserByConditions(String gender, int minAge, int maxAge, String keySkill);
}
