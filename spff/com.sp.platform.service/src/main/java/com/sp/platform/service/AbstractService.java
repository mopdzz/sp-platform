package com.sp.platform.service;

import com.sp.platform.common.PageView;
import com.yangl.common.hibernate.PaginationSupport;
import org.hibernate.criterion.Order;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yfyanglei
 * Date: 13-4-6
 * Time: 下午10:23
 * To change this template use File | Settings | File Templates.
 */
public interface AbstractService<T> {
    public static Order[] orders = new Order[]{Order.asc("id")};

    public T get(int id);
    public void delete(int id);
    public void save(T object);
    public List<T> getAll();
    public PaginationSupport getPage(PaginationSupport page,Order[] orders, PageView pageView);
}
