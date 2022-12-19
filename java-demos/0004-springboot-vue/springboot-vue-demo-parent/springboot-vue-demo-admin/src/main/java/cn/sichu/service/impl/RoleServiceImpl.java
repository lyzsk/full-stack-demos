package cn.sichu.service.impl;

import cn.sichu.entity.Role;
import cn.sichu.mapper.RoleMapper;
import cn.sichu.service.IRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sichu
 * @since 2022-12-16
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Override
    public List<Role> listRolesByUserId(Long userId) {

        List<Role> roles = this.list(
            new QueryWrapper<Role>().inSql("id", "select role_id from sys_user_role where user_id = " + userId));

        return roles;
    }
}
