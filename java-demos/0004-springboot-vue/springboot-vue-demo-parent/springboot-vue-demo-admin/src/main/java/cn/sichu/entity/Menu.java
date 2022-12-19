package cn.sichu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author sichu
 * @since 2022-12-16
 */
@TableName("sys_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父菜单ID，一级菜单为0
     */
    @NotNull(message = "上级菜单不能为空")
    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    private String name;

    /**
     * 菜单URL
     */
    private String path;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    @NotBlank(message = "菜单授权码不能为空")
    private String perms;

    private String component;

    /**
     * 类型     0：目录   1：菜单   2：按钮
     */
    @NotNull(message = "菜单类型不能为空")
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer orderNum;

    private LocalDateTime created;

    private LocalDateTime updated;

    private Integer status;

    /**
     * 从dto转树状结构的时候, 返回的是children数组, 而不是parentId
     */
    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();

    public Menu() {
    }

    public Menu(Long id, Long parentId, String name, String path, String perms, String component, Integer type,
        String icon, Integer orderNum, LocalDateTime created, LocalDateTime updated, Integer status) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.path = path;
        this.perms = perms;
        this.component = component;
        this.type = type;
        this.icon = icon;
        this.orderNum = orderNum;
        this.created = created;
        this.updated = updated;
        this.status = status;
    }

    public Menu(Long id, Long parentId, String name, String path, String perms, String component, Integer type,
        String icon, Integer orderNum, LocalDateTime created, LocalDateTime updated, Integer status,
        List<Menu> children) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.path = path;
        this.perms = perms;
        this.component = component;
        this.type = type;
        this.icon = icon;
        this.orderNum = orderNum;
        this.created = created;
        this.updated = updated;
        this.status = status;
        this.children = children;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Menu{" + "id=" + id + ", parentId=" + parentId + ", name=" + name + ", path=" + path + ", perms="
            + perms + ", component=" + component + ", type=" + type + ", icon=" + icon + ", orderNum=" + orderNum
            + ", created=" + created + ", updated=" + updated + ", status=" + status + "}";
    }
}
