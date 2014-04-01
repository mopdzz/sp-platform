package com.sp.platform.dao;

import com.yangl.common.hibernate.HibernateDaoUtil;
import com.sp.platform.entity.Menu;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mopdgg
 * Date: 13-3-30
 * Time: 下午10:55
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class MenuDao extends HibernateDaoUtil<Menu, Integer> {
    /**
     * 读取顶级树节点. 根据参数排序
     */
    public List getMainMenus(String sort, String sortType) {
        DetachedCriteria dc = DetachedCriteria.forClass(Menu.class);
        dc.add(Restrictions.eq("parent.id", 0));
        if (sortType.equals("asc")) {
            dc.addOrder(Order.asc(sort));
        } else {
            dc.addOrder(Order.desc(sort));
        }
        return findByCriteria(dc);
    }

    /**
     * 读取用户可见的菜单.
     *
     * @param roleId 用户id
     * @return menu id list
     */
    public List<Integer> loadUserMenuIds(Integer roleId) {
        String sql = "select distinct m.id from tbl_menu m,tbl_role_menu rm where m.id=rm.menuid and rm.roleid=:roleid";
        Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql).setParameter("roleid", roleId);
        return query.list();
    }
}
