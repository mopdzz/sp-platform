package com.sp.platform.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: mopdgg
 * Date: 13-3-30
 * Time: 下午10:47
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "tbl_menu")
public class Menu extends BaseEntity {
    private String name; //菜单名称
    private String url; //菜单地址
    private String target; //指向Iframe
    private String icon; //图标
    private int sort; //排序

    private Menu parent;
    private Set<Menu> children = new HashSet<Menu>(0); // 子节点

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", insertable = false, updatable = false)
    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    // 多对多定义
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @OrderBy("sort asc, id desc")
    public Set<Menu> getChildren() {
        return children;
    }

    public void setChildren(Set<Menu> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "sort=" + sort +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", target='" + target + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
