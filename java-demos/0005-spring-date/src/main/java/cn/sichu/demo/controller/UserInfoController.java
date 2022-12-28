package cn.sichu.demo.controller;

import cn.sichu.demo.entity.UserInfo;
import cn.sichu.demo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author sichu
 * @date 2022/12/27
 **/
@RestController
@RequestMapping("/userinfo")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/insert")
    public void insert(@RequestBody UserInfo userInfo) {
        int result = userInfoService.insert(userInfo);
    }

    @PostMapping("/batch-insert")
    public void batchInsert(@RequestBody List<UserInfo> userInfoList) {
        int result = userInfoService.batchInsert(userInfoList);
    }

    @GetMapping("/list")
    public List<UserInfo> getList() {
        List<UserInfo> list = userInfoService.getList();
        return list;
    }

    @GetMapping("/list-with-json-format")
    public List<UserInfo> getListWithJsonFormat() {
        List<UserInfo> list = userInfoService.getListWithJsonFormat();
        return list;
    }

    @GetMapping("/list-with-sdf")
    public List<UserInfo> getListWithSDF() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<UserInfo> list = userInfoService.getListWithSDF();
        list.forEach(item -> {
            item.setCTime(simpleDateFormat.format(item.getCreateTime()));
            item.setUTime(simpleDateFormat.format(item.getUpdateTime()));
        });
        return list;
    }

    // @GetMapping("/list-with-dtf")
    // public List<UserInfo> getListWithDTF() {
    //     DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //     List<UserInfo> list = userInfoService.getListWithDTF();
    //     list.forEach(item -> {
    //         item.setCTime(dateTimeFormatter.format(item.getCreateTime()));
    //         item.setUTime(dateTimeFormatter.format(item.getUpdateTime()));
    //     });
    //     return list;
    // }

    @GetMapping("/list-with-jackson")
    public List<UserInfo> getListWithJackson() {
        List<UserInfo> list = userInfoService.getListWithJackson();
        return list;
    }
}
