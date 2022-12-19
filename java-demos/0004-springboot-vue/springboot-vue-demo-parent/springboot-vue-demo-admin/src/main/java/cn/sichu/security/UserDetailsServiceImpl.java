package cn.sichu.security;

import cn.sichu.entity.User;
import cn.sichu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sichu
 * @date 2022/12/18
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码不正确");
        }

        return new AccountUser(user.getId(), user.getUsername(), user.getPassword(), getUserAuthority(user.getId()));
    }

    /**
     * 获取用户权限信息, 包括角色, 菜单权限
     *
     * @param userId
     * @return List<GrantedAuthority>
     */
    public List<GrantedAuthority> getUserAuthority(Long userId) {
        // 角色, 菜单操作权限
        // ROLE_admin, sys:user:list, ...
        String authority = userService.getUserAuthorityInfo(userId);

        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
