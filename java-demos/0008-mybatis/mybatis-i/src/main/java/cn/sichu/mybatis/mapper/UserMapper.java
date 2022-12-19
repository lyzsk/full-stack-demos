package cn.sichu.mybatis.mapper;

import cn.sichu.mybatis.pojo.User;

import java.util.List;

/**
 * @author sichu
 * @date 2022/12/04
 **/
public interface UserMapper {
    /**
     * 添加用户信息
     */
    int insertUser();

    /**
     * 删除用户信息
     */
    int deleteUser();

    /**
     * 修改用户信息
     */
    int updateUser();

    /**
     * 根据id查询用户
     */
    User getUserById();

    /**
     * 查询所有用户
     */
    List<User> getAllUser();
}
