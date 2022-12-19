package cn.sichu.service;

import cn.sichu.dto.MenuDto;
import cn.sichu.entity.Menu;
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
public interface IMenuService extends IService<Menu> {

    List<MenuDto> getCurrentUserNav();

    List<Menu> tree();
}
