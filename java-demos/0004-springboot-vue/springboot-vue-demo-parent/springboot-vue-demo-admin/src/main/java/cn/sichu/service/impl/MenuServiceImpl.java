package cn.sichu.service.impl;

import cn.hutool.json.JSONUtil;
import cn.sichu.dto.MenuDto;
import cn.sichu.entity.Menu;
import cn.sichu.entity.User;
import cn.sichu.mapper.MenuMapper;
import cn.sichu.mapper.UserMapper;
import cn.sichu.service.IMenuService;
import cn.sichu.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Lazy
    @Autowired
    IUserService userService;

    @Autowired
    UserMapper userMapper;

    @Override
    public List<MenuDto> getCurrentUserNav() {
        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        List<Long> navMenuIds = userMapper.getNavMenuIds(user.getId());
        List<Menu> menus = this.listByIds(navMenuIds);
        // 转树状结构
        List<Menu> menuTree = buildTreeMenu(menus);
        // 实体转dto
        return convert(menuTree);
    }

    @Override
    public List<Menu> tree() {
        // 获取所有菜单信息
        List<Menu> menus = this.list(new QueryWrapper<Menu>().orderByAsc("order_num"));

        // 转成树状结构
        return buildTreeMenu(menus);
    }

    private List<MenuDto> convert(List<Menu> menuTree) {
        List<MenuDto> menuDtos = new ArrayList<>();
        menuTree.forEach(m -> {
            MenuDto dto = new MenuDto();
            dto.setId(m.getId());
            dto.setName(m.getPerms());
            dto.setTitle(m.getName());
            dto.setComponent(m.getComponent());
            dto.setPath(m.getPath());
            if (m.getChildren().size() > 0) {
                dto.setChildren(convert(m.getChildren()));
            }
            menuDtos.add(dto);
        });
        return menuDtos;
    }

    private List<Menu> buildTreeMenu(List<Menu> menus) {
        List<Menu> finalMenus = new ArrayList<>();
        // 先找到各自的children
        for (Menu menu : menus) {
            for (Menu m : menus) {
                if (menu.getId().equals(m.getParentId())) {
                    menu.getChildren().add(m);
                }
            }
            if (menu.getParentId() == 0L) {
                finalMenus.add(menu);
            }
        }
        System.out.println(JSONUtil.toJsonStr(finalMenus));
        // 再找到parent
        return finalMenus;
    }
}
