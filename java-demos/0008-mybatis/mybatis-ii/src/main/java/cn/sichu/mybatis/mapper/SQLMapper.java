package cn.sichu.mybatis.mapper;

import cn.sichu.mybatis.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sichu
 * @date 2022/12/05
 **/
public interface SQLMapper {
    /**
     * 根据用户名模糊查询用户信息
     */
    List<User> getUserByFuzzy(@Param("username") String username);

    /**
     * 批量删除
     */
    int batchDelete(@Param("ids") String ids);

    /**
     * 查询指定表名中的数据
     */
    List<User> getUserByTablename(@Param("tablename") String tablename);
}
