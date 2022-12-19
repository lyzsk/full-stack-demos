package cn.sichu.service;

import cn.sichu.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sichu
 * @since 2022-12-16
 */
public interface IRoleService extends IService<Role> {
    List<Role> listRolesByUserId(Long userId);
}
