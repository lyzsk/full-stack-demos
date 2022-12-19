package cn.sichu.springboot.helloworld.controller;

import cn.sichu.springboot.helloworld.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sichu
 * @date 2022/11/27
 **/
@RestController
public class ParamController {
    @PostMapping("/postTest1")
    public String postTest1() {
        return "POST请求, PostMapping";
    }

    @RequestMapping(value = "/postTest2", method = RequestMethod.POST)
    public String postTest2() {
        return "POST请求, RequestMapping";
    }

    @PostMapping("/postTest3")
    public String postTest3(User user) {
        System.out.println(user);
        return "POST请求, User user";
    }

    @PostMapping("/postTest4")
    private User postTest4(User user) {
        return user;
    }
}
