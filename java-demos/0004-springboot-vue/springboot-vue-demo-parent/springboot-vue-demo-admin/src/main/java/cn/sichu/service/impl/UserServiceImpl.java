package cn.sichu.service.impl;

import cn.sichu.entity.Menu;
import cn.sichu.entity.Role;
import cn.sichu.entity.User;
import cn.sichu.mapper.UserMapper;
import cn.sichu.service.IMenuService;
import cn.sichu.service.IRoleService;
import cn.sichu.service.IUserService;
import cn.sichu.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sichu
 * @since 2022-12-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    IRoleService roleService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    IMenuService menuService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public User getByUsername(String username) {
        return getOne(new QueryWrapper<User>().eq("username", username));
    }

    @Override
    public String getUserAuthorityInfo(Long userId) {
        User user = userMapper.selectById(userId);
        String authority = "";
        // 加一层缓存, 减少访问数据库次数
        if (redisUtil.hasKey("GrantedAuthority:" + user.getUsername())) {
            authority = (String)redisUtil.get("GrantedAuthority:" + user.getUsername());
        } else {

            // 获取角色
            List<Role> roles = roleService.list(
                new QueryWrapper<Role>().inSql("id", "select role_id from sys_user_role where user_id = " + userId));
            if (roles.size() > 0) {
                String roleCodes = roles.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
            }
            // 获取菜单操作编码
            List<Long> menuIds = userMapper.getNavMenuIds(userId);
            if (menuIds.size() > 0) {
                List<Menu> menus = menuService.listByIds(menuIds);
                String menuPerms = menus.stream().map(m -> m.getPerms()).collect(Collectors.joining(","));
                authority = authority.concat(menuPerms);
            }
            // redis 缓存 60 分钟
            redisUtil.set("GrantedAuthority:" + user.getUsername(), authority, 60 * 60);
        }
        return authority;
    }

    @Override
    public void clearUserAuthorityInfo(String username) {
        redisUtil.del("GrantedAuthority:" + username);
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId) {
        List<User> users = this.list(
            new QueryWrapper<User>().inSql("id", "select user_id from sys_user_role where role_id = " + roleId));
        users.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }

    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {
        List<User> users = userMapper.listByMenuId(menuId);
        users.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }
}
