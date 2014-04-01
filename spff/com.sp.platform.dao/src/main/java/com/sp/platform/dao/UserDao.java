package com.sp.platform.dao;

import com.yangl.common.hibernate.HibernateDaoUtil;
import com.sp.platform.entity.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdgg
 * Date: 13-3-30
 * Time: 下午10:54
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class UserDao extends HibernateDaoUtil<User, Integer> {

    private static final String UPDATE_PASSWD = "update tbl_user set passwd=:passwd where id=:id";

    public void updatePasswd(int id, String passwd) {
        getSession().createSQLQuery(UPDATE_PASSWD).setParameter("id", id).setParameter("passwd", passwd).executeUpdate();
    }

    public List<User> getByRole(int role) {
        DetachedCriteria dc = DetachedCriteria.forClass(User.class);
        dc.add(Restrictions.eq("role", role));
        return findByCriteria(dc);
    }
}
