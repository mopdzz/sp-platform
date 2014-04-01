package com.sp.platform.service;

import com.sp.platform.dao.NaHaoduanDao;
import com.sp.platform.entity.NaHaoduan;
import com.yangl.common.hibernate.PaginationSupport;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: yangl
 * Date: 13-6-23 下午3:40
 */
@Service
@Transactional
public class NaHaoduanService {
    @Autowired
    private NaHaoduanDao naHaoduanDao;

    @Transactional(readOnly = true)
    public PaginationSupport getPage(PaginationSupport page, Order[] orders1) {
        DetachedCriteria dc = DetachedCriteria.forClass(NaHaoduan.class);
        return naHaoduanDao.findPageByCriteria(dc, page, orders1);
    }

    public List<NaHaoduan> getAll() {
        return naHaoduanDao.getAll();
    }

    public void delete(Integer id) {
        naHaoduanDao.delete(id);
    }
}
