package cn.sichu.demo.service.iml;

import cn.sichu.demo.entity.UserInfo;
import cn.sichu.demo.mapper.UserInfoMapper;
import cn.sichu.demo.service.UserInfoService;
import cn.sichu.demo.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author sichu
 * @date 2022/12/27
 **/
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IdWorker idWorker;

    @Override
    public int insert(UserInfo userInfo) {
        userInfo.setId(idWorker.nextId());
        userInfo.setCreateTime(new Date(System.currentTimeMillis()));
        userInfo.setUpdateTime(new Date(System.currentTimeMillis()));
        // userInfo.setCreateTime(LocalDateTime.now());
        // userInfo.setUpdateTime(LocalDateTime.now());
        return userInfoMapper.insert(userInfo);
    }

    @Override
    public int batchInsert(List<UserInfo> userInfoList) {
        for (UserInfo userInfo : userInfoList) {
            userInfo.setId(idWorker.nextId());
            userInfo.setCreateTime(new Date(System.currentTimeMillis()));
            userInfo.setUpdateTime(new Date(System.currentTimeMillis()));
            // userInfo.setCreateTime(LocalDateTime.now());
            // userInfo.setUpdateTime(LocalDateTime.now());
        }
        return userInfoMapper.batchInsert(userInfoList);
    }

    @Override
    public List<UserInfo> getList() {
        return userInfoMapper.getList();
    }

    @Override
    public List<UserInfo> getListWithJsonFormat() {
        return userInfoMapper.getListWithJsonFormat();
    }

    @Override
    public List<UserInfo> getListWithSDF() {
        return userInfoMapper.getListWithSDF();
    }

    @Override
    public List<UserInfo> getListWithDTF() {
        return userInfoMapper.getListWithDTF();
    }

    @Override
    public List<UserInfo> getListWithJackson() {
        return userInfoMapper.getListWithJackson();
    }

}
