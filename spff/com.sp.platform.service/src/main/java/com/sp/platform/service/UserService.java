package com.sp.platform.service;

import com.sp.platform.common.PageView;
import com.sp.platform.entity.User;
import com.yangl.common.hibernate.PaginationSupport;
import org.hibernate.criterion.Order;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdgg
 * Date: 13-3-30
 * Time: 下午11:45
 * To change this template use File | Settings | File Templates.
 */
public interface UserService {

    public User get(int id);
    public void save(User user);
    public void delete(int id);
    public PaginationSupport getUsers(PaginationSupport page);
    public User checkUser(User user);
    public List<User> getCpList();
    public List<User> getAll();
    public List<User> getByRole(int role);

    public PaginationSupport getPage(PaginationSupport page,Order[] orders, PageView pageView);

    public void updatePasswd(int id, String passwd);
}
