package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.CpNumDao;
import com.sp.platform.dao.UserDao;
import com.sp.platform.entity.CpNum;
import com.sp.platform.entity.User;
import com.sp.platform.service.UserService;
import com.yangl.common.hibernate.PaginationSupport;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdgg
 * Date: 13-3-30
 * Time: 下午11:45
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private CpNumDao cpNumDao;
    @Autowired
    private CpNumServiceImpl cpNumService;

    @Override
    @Transactional(readOnly = true)
    public User get(int id) {
        return userDao.get(id);
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public void delete(int id) {
        DetachedCriteria dc = DetachedCriteria.forClass(CpNum.class);
        dc.add(Restrictions.eq("cpid", id));
        List<CpNum> temp = cpNumDao.findByCriteria(dc);
        if (temp != null && temp.size() > 0) {
            for(CpNum cpNum : temp){
                cpNumService.delete(cpNum);
            }
        }
        userDao.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationSupport getUsers(PaginationSupport page) {
        DetachedCriteria dc = DetachedCriteria.forClass(User.class);
        return userDao.findPageByCriteria(dc, page, null);
    }

    @Override
    @Transactional(readOnly = true)
    public User checkUser(User user) {
        User dbUser = userDao.findUniqueBy("name", user.getName());
        if(dbUser != null && dbUser.getPasswd().equalsIgnoreCase(user.getPasswd()) && dbUser.getStatus() == 1){
            return dbUser;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getCpList() {
        DetachedCriteria dc = DetachedCriteria.forClass(User.class);
        dc.add(Restrictions.in("role", new Integer[]{10, 11}));
        return userDao.findByCriteria(dc, new Order[]{Order.asc("parentId"), Order.desc("id")});
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getByRole(int role) {
        DetachedCriteria dc = DetachedCriteria.forClass(User.class);
        dc.add(Restrictions.eq("role", role));
        return userDao.findByCriteria(dc);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Transactional(readOnly = true)
    public PaginationSupport getPage(PaginationSupport page,Order[] orders, PageView pageView){
        DetachedCriteria dc = DetachedCriteria.forClass(User.class);
        if (pageView != null){
            if(pageView.getParentId() > 0){
                dc.add(Restrictions.eq("parentId", pageView.getParentId()));
            }
            if(pageView.getRole() > 0){
                dc.add(Restrictions.eq("role", pageView.getRole()));
            }
        }
        return userDao.findPageByCriteria(dc, page, orders);
    }

    @Override
    public void updatePasswd(int id, String passwd) {
        userDao.updatePasswd(id, passwd);
    }

}
