package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.ProvReduceDao;
import com.sp.platform.entity.ProvReduce;
import com.sp.platform.service.ProvReduceService;
import com.yangl.common.hibernate.PaginationSupport;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: yangl
 * Date: 13-5-26 下午4:11
 */
@Service
@Transactional
public class ProvReduceServiceImpl implements ProvReduceService {
    @Autowired
    private ProvReduceDao provReduceDao;

    @Override
    @Transactional(readOnly = true)
    public ProvReduce get(int id) {
        return provReduceDao.get(id);
    }

    @Override
    public void delete(int id) {
        provReduceDao.delete(id);
    }

    @Override
    public void save(ProvReduce object) {
        provReduceDao.save(object);
    }

    @Override
    public List<ProvReduce> getAll() {
        return provReduceDao.getAll();
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        DetachedCriteria dc = DetachedCriteria.forClass(ProvReduce.class);
        if (pageView != null && pageView.getSpid() > 0){
            dc.add(Restrictions.eq("spid", pageView.getSpid()));
        }
        return provReduceDao.findPageByCriteria(dc, page, orders);
    }

    @Override
    public List getByCalled(String called) {
        DetachedCriteria dc = DetachedCriteria.forClass(ProvReduce.class);
        dc.add(Restrictions.eq("called", called));
        return provReduceDao.findByCriteria(dc);
    }

    @Override
    public List getByCpid(Integer id) {
        String hql = "select prov from User user, CpNum cpnum, ProvReduce prov where user.id=cpnum.cpid and cpnum.called=prov.called and (user.id=? or user.parentId=?)";
        return provReduceDao.find(hql, id, id);
    }
}
