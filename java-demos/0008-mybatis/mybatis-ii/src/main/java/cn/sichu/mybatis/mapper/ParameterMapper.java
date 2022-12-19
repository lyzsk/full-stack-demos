package cn.sichu.mybatis.mapper;

import cn.sichu.mybatis.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author sichu
 * @date 2022/12/05
 **/
public interface ParameterMapper {
    /**
     * 用@Param验证登录
     */
    User checkLoginByParam(@Param("username") String username, @Param("password") String password);

    /**
     * 根据User对象Parameter添加用户信息
     */
    int insertUserWithParameter(User user);

    /**
     * 用Map为参数验证登录
     */
    User checkLoginByMap(Map<String, Object> map);

    /**
     * 根据用户名查询用户信息
     */
    User getUserByUsername(String username);

    /**
     * 不用@Param验证登录
     */
    User checkLoginWithoutParam(String username, String password);
}
