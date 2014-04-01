package com.sp.platform.service;

import com.sp.platform.entity.Menu;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdgg
 * Date: 13-3-30
 * Time: 下午11:45
 * To change this template use File | Settings | File Templates.
 */
public interface MenuService {
    /**
     * 根据用户所属角色ID，构建左边菜单(js生成树)
     *
     * @param roleId
     * @return
     */
    public String buildMenuString(int roleId);

    /**
     * 根据角色ID，取得用户的菜单列表
     * @param roleId
     * @return
     */
    public List<Menu> getUserMenus(int roleId);
}
