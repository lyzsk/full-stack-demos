package cn.sichu.demo.mapper;

import cn.sichu.demo.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author sichu
 * @date 2022/12/29
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
