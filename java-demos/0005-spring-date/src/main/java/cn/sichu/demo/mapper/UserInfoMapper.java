package cn.sichu.demo.mapper;

import cn.sichu.demo.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author sichu
 * @date 2022/12/27
 **/
@Mapper
public interface UserInfoMapper {

    public int insert(UserInfo userInfo);

    public int batchInsert(List<UserInfo> userInfoList);

    public List<UserInfo> getList();

    public List<UserInfo> getListWithJsonFormat();

    public List<UserInfo> getListWithSDF();

    public List<UserInfo> getListWithDTF();

    public List<UserInfo> getListWithJackson();
}
