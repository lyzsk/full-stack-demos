package cn.sichu.controller;

import cn.hutool.core.util.StrUtil;
import cn.sichu.common.Const;
import cn.sichu.common.Result;
import cn.sichu.entity.Role;
import cn.sichu.entity.RoleMenu;
import cn.sichu.entity.UserRole;
import cn.sichu.service.IRoleMenuService;
import cn.sichu.service.IRoleService;
import cn.sichu.service.IUserRoleService;
import cn.sichu.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sichu
 * @since 2022-12-16
 */
@RestController
@RequestMapping("/sys/role")
public class RoleController {
    @Autowired
    HttpServletRequest request;

    @Autowired
    IRoleService roleService;

    @Autowired
    IRoleMenuService roleMenuService;

    @Autowired
    IUserService userService;

    @Autowired
    IUserRoleService userRoleService;

    private Page getPage() {
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int size = ServletRequestUtils.getIntParameter(request, "size", 10);
        return new Page(current, size);
    }

    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result info(@PathVariable("id") Long id) {
        Role role = roleService.getById(id);
        List<RoleMenu> roleMenus = roleMenuService.list(new QueryWrapper<RoleMenu>().eq("role_id", id));
        List<Long> menuIds = roleMenus.stream().map(p -> p.getMenuId()).collect(Collectors.toList());
        role.setMenuIds(menuIds);
        return Result.success(role);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result list(String name) {
        Page<Role> pageData =
            roleService.page(getPage(), new QueryWrapper<Role>().eq(StrUtil.isNotBlank(name), "name", name));
        return Result.success(pageData);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:role:save')")
    public Result save(@Validated @RequestBody Role role) {
        role.setCreated(LocalDateTime.now());
        role.setStatus(Const.STATUS_ON);
        roleService.save(role);
        return Result.success(role);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public Result update(@Validated @RequestBody Role role) {

        role.setUpdated(LocalDateTime.now());
        roleService.updateById(role);
        // 更新缓存
        userService.clearUserAuthorityInfoByRoleId(role.getId());
        return Result.success(role);
    }

    /**
     * 因为有可能删了多个id, 要添加事务
     *
     * @param ids
     * @return ""
     */
    @Transactional
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public Result info(@RequestBody Long[] ids) {

        roleService.removeByIds(Arrays.asList(ids));

        // 删除中间表
        userRoleService.remove(new QueryWrapper<UserRole>().in("role_id", ids));
        roleMenuService.remove(new QueryWrapper<RoleMenu>().in("role_id", ids));

        // 缓存同步删除
        Arrays.stream(ids).forEach(id -> {
            // 更新缓存
            userService.clearUserAuthorityInfoByRoleId(id);
        });

        return Result.success("");
    }

    @Transactional
    @PostMapping("/perm/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:perm')")
    public Result info(@PathVariable("roleId") Long roleId, @RequestBody Long[] menuIds) {
        List<RoleMenu> roleMenus = new ArrayList<>();
        Arrays.stream(menuIds).forEach(menuId -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);
            roleMenus.add(roleMenu);
        });
        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id", roleId));
        roleMenuService.saveBatch(roleMenus);
        userService.clearUserAuthorityInfoByRoleId(roleId);
        return Result.success(menuIds);
    }
}
