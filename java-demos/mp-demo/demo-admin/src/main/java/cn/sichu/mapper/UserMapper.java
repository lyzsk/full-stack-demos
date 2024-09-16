package cn.sichu.mapper;

import cn.sichu.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sichu huang
 * @date 2024/09/16
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<User> selectUserByKeySkills(@Param("firstSkill") String firstSkill, @Param("secondSkill") String secondSkill);
}
