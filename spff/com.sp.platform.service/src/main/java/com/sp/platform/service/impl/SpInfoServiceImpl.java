package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.CpNumDao;
import com.sp.platform.dao.ServiceNumDao;
import com.sp.platform.dao.SpInfoDao;
import com.sp.platform.entity.ServiceNum;
import com.sp.platform.entity.SpInfo;
import com.sp.platform.service.SpInfoService;
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
 * Created with IntelliJ IDEA.
 * User: yfyanglei
 * Date: 13-4-6
 * Time: 下午10:27
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class SpInfoServiceImpl implements SpInfoService {
    @Autowired
    private SpInfoDao spInfoDao;
    @Autowired
    private ServiceNumDao serviceNumDao;
    @Autowired
    private ServiceNumServiceImpl serviceNumService;

    @Override
    @Transactional(readOnly = true)
    public SpInfo get(int id) {
        return spInfoDao.get(id);
    }

    @Override
    public void delete(int id) {
        DetachedCriteria dc = DetachedCriteria.forClass(ServiceNum.class);
        dc.add(Restrictions.eq("spid", id));
        List<ServiceNum> temp = serviceNumDao.findByCriteria(dc);
        if (temp != null && temp.size() > 0) {
            for (ServiceNum serviceNum : temp) {
                serviceNumService.delete(serviceNum);
            }
        }
        spInfoDao.delete(id);
    }

    @Override
    public void save(SpInfo object) {
        spInfoDao.save(object);
    }

    @Override
    public List<SpInfo> getAll() {
        return spInfoDao.getAll();
    }

    public List<SpInfo> getAll(String type) {
        return spInfoDao.findBy("type", type);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationSupport getPage(PaginationSupport page, Order[] orders1, PageView pageView) {
        DetachedCriteria dc = DetachedCriteria.forClass(SpInfo.class);
        return spInfoDao.findPageByCriteria(dc, page, orders1);
    }

    public static void main(String[] args) {
        String called="10661388_1";
        System.out.println(StringUtils.endsWith(called, "*"));
    }
}
