package com.sp.platform.dao;

import com.sp.platform.entity.Haoduan;
import com.yangl.common.hibernate.HibernateDaoUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: yangl
 * Date: 13-5-26 下午3:45
 */
@Repository
public class HaoduanDao extends HibernateDaoUtil<Haoduan, Integer> {

    public List<Haoduan> getHaoduansGtId(int id) {
        DetachedCriteria dc = DetachedCriteria.forClass(Haoduan.class);
        dc.add(Restrictions.gt("id", id));
        dc.addOrder(Order.asc("id"));
        return findByCriteria(dc);
    }
}
