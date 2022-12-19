package cn.sichu.mybatis.plus.service.impl;

import cn.sichu.mybatis.plus.mapper.UserMapper;
import cn.sichu.mybatis.plus.pojo.User;
import cn.sichu.mybatis.plus.service.UserService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author sichu
 * @date 2022/12/05
 **/
@DS("master")
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
