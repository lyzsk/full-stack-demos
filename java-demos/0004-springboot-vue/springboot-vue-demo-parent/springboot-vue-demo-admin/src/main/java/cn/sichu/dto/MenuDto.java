package cn.sichu.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * {
 * name: "SysUser",
 * title: "用户管理",
 * icon: "el-icon-s-custom",
 * component: "sys/User",
 * path: "/sys/users",
 * children: [],
 * },
 *
 * @author sichu
 * @date 2022/12/19
 **/
public class MenuDto implements Serializable {
    private Long id;
    private String name;
    private String title;
    private String icon;
    private String path;
    private String component;
    private List<MenuDto> children = new ArrayList<>();

    public MenuDto() {
    }

    public MenuDto(Long id, String name, String title, String icon, String path, String component,
        List<MenuDto> children) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.icon = icon;
        this.path = path;
        this.component = component;
        this.children = children;
    }

    @Override
    public String toString() {
        return "MenuDto{" + "id=" + id + ", name='" + name + '\'' + ", title='" + title + '\'' + ", icon='" + icon
            + '\'' + ", path='" + path + '\'' + ", component='" + component + '\'' + ", children=" + children + '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public List<MenuDto> getChildren() {
        return children;
    }

    public void setChildren(List<MenuDto> children) {
        this.children = children;
    }
}
