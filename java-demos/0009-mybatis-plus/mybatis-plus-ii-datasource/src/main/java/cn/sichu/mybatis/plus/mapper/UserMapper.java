package cn.sichu.mybatis.plus.mapper;

import cn.sichu.mybatis.plus.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author sichu
 * @date 2022/12/05
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
