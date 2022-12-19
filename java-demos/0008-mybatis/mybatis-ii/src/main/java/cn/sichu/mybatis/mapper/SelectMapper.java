package cn.sichu.mybatis.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author sichu
 * @date 2022/12/05
 **/
public interface SelectMapper {
    /**
     * 查询用户信息的总记录数
     */
    Integer getCount();

    /**
     * 根据id返回map类型的用户信息
     * <p>
     * "@MapKey("")" 可以删掉
     */
    @MapKey("")
    Map<String, Object> getUserByIdToMap(@Param("id") Integer id);

    /**
     * 查询所有用户信息为map集合
     */
    @MapKey("id")
    Map<String, Object> getAllUserToMap();
}
