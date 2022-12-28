package cn.sichu.demo.service;

import cn.sichu.demo.entity.UserInfo;

import java.util.List;

/**
 * @author sichu
 * @date 2022/12/27
 **/
public interface UserInfoService {

    public int insert(UserInfo userInfo);

    public int batchInsert(List<UserInfo> userInfoList);

    public List<UserInfo> getList();

    public List<UserInfo> getListWithJsonFormat();

    public List<UserInfo> getListWithSDF();

    public List<UserInfo> getListWithDTF();

    public List<UserInfo> getListWithJackson();
}
