package cn.sichu.demo.service.impl;

import cn.sichu.demo.entity.User;
import cn.sichu.demo.mapper.UserMapper;
import cn.sichu.demo.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author sichu
 * @date 2022/12/29
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
