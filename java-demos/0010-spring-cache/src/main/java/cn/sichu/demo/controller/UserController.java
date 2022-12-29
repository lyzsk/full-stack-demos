package cn.sichu.demo.controller;

import cn.sichu.demo.entity.User;
import cn.sichu.demo.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sichu
 * @date 2022/12/29
 **/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private UserService userService;

    @CachePut(value = "userCache", key = "#user.id")
    @PostMapping
    public User save(User user) {
        userService.save(user);
        return user;
    }

    @CacheEvict(value = "userCache", key = "#id")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.removeById(id);
    }

    @CacheEvict(value = "userCache", key = "#user.id")
    @PutMapping
    public User update(User user) {
        userService.updateById(user);
        return user;
    }

    // @Cacheable(value = "userCache", key = "#id", condition = "#root.args[0] != null")
    @Cacheable(value = "userCache", key = "#id", unless = "#result == null")
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return user;
    }

    @Cacheable(value = "userCache", key = "#user.id + '_' + #user.name")
    @GetMapping("/list")
    public List<User> list(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(user.getId() != null, User::getId, user.getId())
            .eq(user.getName() != null, User::getName, user.getName());
        return userService.list(queryWrapper);
    }
}
