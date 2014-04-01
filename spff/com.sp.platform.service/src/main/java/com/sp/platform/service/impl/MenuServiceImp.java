package com.sp.platform.service.impl;

import com.sp.platform.dao.MenuDao;
import com.sp.platform.entity.Menu;
import com.sp.platform.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: yfyanglei
 * Date: 13-4-4
 * Time: 上午11:27
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional(readOnly = true)
public class MenuServiceImp implements MenuService {
    @Autowired
    private MenuDao menuDao;

    @Override
    public String buildMenuString(int roleId) {
        return buildMenuString(getUserMenus(roleId));
    }

    public List<Menu> getUserMenus(int roleId){
        List<Integer> menuIds = menuDao.loadUserMenuIds(roleId);
        List<Menu> mainMenus = menuDao.getMainMenus("sort", "asc");
        return this.filterMenu(mainMenus, menuIds);
    }

    private List<Menu> filterMenu(Collection<Menu> menus, List<Integer> menuIds) {
        List<Menu> menuList = new ArrayList<Menu>();
        for (Menu m : menus) {
            if (menuIds.contains(m.getId())) {
                menuList.add(m);
                List<Menu> children = filterMenu(m.getChildren(), menuIds);
                m.getChildren().clear();
                m.getChildren().addAll(children);
            }
        }
        return menuList;
    }

    private String buildMenuString(List<Menu> menus) {
        StringBuilder menuBuilder = new StringBuilder();
        menuBuilder.append("var tree = null;");
        menuBuilder.append("var root = new TreeNode('功能导航');");
        for (int i = 0; i < menus.size(); i++) {
            addMenuString("root", i + 1, menus.get(i), menuBuilder);
        }
        menuBuilder.append("tree = new Tree(root);");
        menuBuilder.append("tree.show('menuTree')");
        return menuBuilder.toString();
    }

    private void addMenuString(String parentMenu, int i, Menu menu,
                               StringBuilder builder) {
        String func = "fun" + i;
        Set<Menu> child = menu.getChildren();
        if (child != null && child.size() > 0) {
            builder.append("var ");
            builder.append(func);
            builder.append(" = new TreeNode('");
            builder.append(menu.getName());
            builder.append("');");
        } else {
            builder.append("var ");
            builder.append(func);
            builder.append(" = new TreeNode('");
            builder.append(menu.getName());
            builder.append("', '");
            builder.append(menu.getUrl());
            builder.append("', 'tree_node.gif', null, 'tree_node.gif', null, '");
            builder.append(menu.getTarget());
            builder.append("');");
        }
        Iterator<Menu> im = child.iterator();
        int j = 0;
        while (im.hasNext()) {
            j++;
            addMenuString(func, i * 100 + j, im.next(), builder);
        }
        builder.append(parentMenu).append(".add(").append(func).append(");");
    }
}
