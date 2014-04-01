package com.sp.platform.service.impl;

import com.sp.platform.cache.CpSyncCache;
import com.sp.platform.common.PageView;
import com.sp.platform.dao.CpNumDao;
import com.sp.platform.dao.ProvReduceDao;
import com.sp.platform.entity.CpNum;
import com.sp.platform.service.CpNumService;
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
 * User: yangl
 * Date: 13-5-29 上午12:31
 */
@Service
@Transactional
public class CpNumServiceImpl implements CpNumService {
    @Autowired
    private CpNumDao cpNumDao;
    @Autowired
    private ProvReduceDao provReduceDao;

    @Override
    @Transactional(readOnly = true)
    public CpNum get(int id) {
        return cpNumDao.get(id);
    }

    @Transactional(readOnly = true)
    public CpNum getByCalled(String called) {
        return cpNumDao.findUniqueBy("called", called);
    }

    @Override
    public List<CpNum> getByCpId(Integer id) {
        String hql = "select cpnum from User user, CpNum cpnum where user.id=cpnum.cpid and (user.id=? or user.parentId=?)";
        return cpNumDao.find(hql, id, id);
    }

    @Override
    public void delete(int id) {
        delete(cpNumDao.get(id));
    }

    public void delete(CpNum cpNum) {
        String called = cpNum.getCalled();
        int len = called.length();
        if (StringUtils.endsWith(called, "*")) {
            len--;
            provReduceDao.executeSQL("delete from tbl_province_reduce where " +
                    "left(called," + len + ")='" + called.substring(0, len) + "'");
        } else {
            provReduceDao.executeSQL("delete from tbl_province_reduce where called='" + called + "'");
        }
        cpNumDao.delete(cpNum);
    }

    @Override
    public void save(CpNum object) {
        cpNumDao.save(object);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CpNum> getAll() {
        return cpNumDao.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        DetachedCriteria dc = DetachedCriteria.forClass(CpNum.class);
        if (pageView.getCpid() > 0) {
            dc.add(Restrictions.eq("cpid", pageView.getCpid()));
        }
        page = cpNumDao.findPageByCriteria(dc, page, orders);
        List<CpNum> cpNums = null;
        if (page.getItems() != null) {
            cpNums = page.getItems();
            for (CpNum cpNum : cpNums) {
                cpNum.setCpname(CpSyncCache.getCpName(cpNum.getCpid()));
                cpNum.setCalled(cpNum.getCalled().replace("#","-"));
            }
        }
        page.setItems(cpNums);
        return page;
    }
}
