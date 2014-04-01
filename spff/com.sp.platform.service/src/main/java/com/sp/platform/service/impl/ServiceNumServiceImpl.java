package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.CpNumDao;
import com.sp.platform.dao.ProvReduceDao;
import com.sp.platform.dao.ServiceNumDao;
import com.sp.platform.entity.ServiceNum;
import com.sp.platform.service.ServiceNumService;
import com.yangl.common.hibernate.PaginationSupport;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: mopdgg
 * Date: 13-5-24 下午10:24
 */
@Service
@Transactional
public class ServiceNumServiceImpl implements ServiceNumService {

    @Autowired
    private ServiceNumDao serviceNumDao;
    @Autowired
    private CpNumDao cpNumDao;
    @Autowired
    private ProvReduceDao provReduceDao;

    @Override
    @Transactional(readOnly = true)
    public ServiceNum get(int id) {
        ServiceNum serviceNum = serviceNumDao.get(id);
        return serviceNum;
    }

    @Override
    public void delete(int id) {
        this.delete(serviceNumDao.get(id));
    }

    public void delete(ServiceNum serviceNum){
        String called = serviceNum.getCalled();
        int len = called.length();
        if (StringUtils.endsWith(called, "*")) {
            len--;
            cpNumDao.executeSQL("delete from tbl_cp_called where " +
                    "left(called," + len + ")='" + called.substring(0, len) + "'");
            provReduceDao.executeSQL("delete from tbl_province_reduce where " +
                    "left(called," + len + ")='" + called.substring(0, len) + "'");
        } else {
            cpNumDao.executeSQL("delete from tbl_cp_called where called='" + called + "'");
            provReduceDao.executeSQL("delete from tbl_province_reduce where called='" + called + "'");
        }
        serviceNumDao.delete(serviceNum);
    }

    @Override
    public void save(ServiceNum object) {
        serviceNumDao.save(object);
    }

    @Override
    public List<ServiceNum> getAll() {
        return serviceNumDao.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        DetachedCriteria dc = DetachedCriteria.forClass(ServiceNum.class);
        if (pageView != null && pageView.getSpid() > 0){
            dc.add(Restrictions.eq("spid", pageView.getSpid()));
        }
        return serviceNumDao.findPageByCriteria(dc, page, orders);
    }
}
