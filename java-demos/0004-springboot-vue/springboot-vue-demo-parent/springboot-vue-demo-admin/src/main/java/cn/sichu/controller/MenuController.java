package cn.sichu.controller;

import cn.hutool.core.map.MapUtil;
import cn.sichu.common.Result;
import cn.sichu.dto.MenuDto;
import cn.sichu.entity.Menu;
import cn.sichu.entity.RoleMenu;
import cn.sichu.entity.User;
import cn.sichu.service.IMenuService;
import cn.sichu.service.IRoleMenuService;
import cn.sichu.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sichu
 * @since 2022-12-16
 */
@RestController
@RequestMapping("/sys/menu")
public class MenuController {

    @Autowired
    IUserService userService;

    @Autowired
    IMenuService menuService;

    @Autowired
    IRoleMenuService roleMenuService;

    @GetMapping("/nav")
    public Result nav(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        // 获取权限信息
        String authorityInfo = userService.getUserAuthorityInfo(user.getId());
        String[] authorityInfoArray = StringUtils.tokenizeToStringArray(authorityInfo, ",");
        // 获取导航栏信息
        List<MenuDto> navs = menuService.getCurrentUserNav();

        return Result.success(MapUtil.builder().put("authorities", authorityInfoArray).put("nav", navs).map());
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable(name = "id") Long id) {
        return Result.success(menuService.getById(id));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public Result list() {
        List<Menu> menus = menuService.tree();
        return Result.success(menus);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:menu:save')")
    public Result save(@Validated @RequestBody Menu menu) {
        menu.setCreated(LocalDateTime.now());
        menuService.save(menu);
        return Result.success(menu);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public Result update(@Validated @RequestBody Menu menu) {
        menu.setUpdated(LocalDateTime.now());
        menuService.updateById(menu);
        userService.clearUserAuthorityInfoByMenuId(menu.getId());
        return Result.success(menu);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public Result delete(@PathVariable("id") Long id) {
        int count = (int)menuService.count(new QueryWrapper<Menu>().eq("parent_id", id));
        if (count > 0) {
            return Result.fail("请先删除子菜单");
        }
        userService.clearUserAuthorityInfoByMenuId(id);
        menuService.removeById(id);
        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("menu_id", id));
        return Result.success("");
    }
}
