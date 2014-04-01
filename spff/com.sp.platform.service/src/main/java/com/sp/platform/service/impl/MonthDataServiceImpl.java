package com.sp.platform.service.impl;

import com.sp.platform.dao.MonthDataDao;
import com.sp.platform.entity.MonthData;
import com.sp.platform.service.MonthDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-2-13
 * Time: 下午10:12
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class MonthDataServiceImpl implements MonthDataService {
    @Resource
    private MonthDataDao monthDataDao;
    @Override
    public void save(MonthData monthData) {
        monthDataDao.save(monthData);
    }
}
