package cn.sichu.controller;

import cn.sichu.common.Result;
import cn.sichu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sichu
 * @date 2022/12/16
 **/
@RestController
public class TestController {
    @Autowired
    IUserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    // @GetMapping("/test")
    // public Object test() {
    //     return userService.list();
    // }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/test")
    public Result test() {
        return Result.success(userService.list());
    }

    /**
     * 超级管理员, 普通用户
     *
     * @return Result
     */
    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/test/pass")
    public Result pass() {
        String password = bCryptPasswordEncoder.encode("666666");
        boolean matches = bCryptPasswordEncoder.matches("666666", password);
        System.out.println("匹配结果: " + matches);
        return Result.success(password);
    }
}
