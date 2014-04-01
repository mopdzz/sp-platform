package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.BillLogDao;
import com.sp.platform.service.SysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: yangl
 * Date: 13-6-4 上午1:29
 */
@Service
@Transactional
public class SysServiceImpl implements SysService {
    @Autowired
    private BillLogDao billLogDao;

    @Override
    public List select(PageView pageView) {
        return billLogDao.getSession().createSQLQuery(pageView.getPasswd()).list();
    }

    @Override
    public int exec(PageView pageView) {
        return billLogDao.getSession().createSQLQuery(pageView.getName()).executeUpdate();
    }
}
