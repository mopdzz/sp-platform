package com.sp.platform.service.impl;

import com.sp.platform.cache.CpSyncCache;
import com.sp.platform.common.Constants;
import com.sp.platform.common.GroupType;
import com.sp.platform.common.PageView;
import com.sp.platform.dao.BillLogDao;
import com.sp.platform.dao.BillTempDao;
import com.sp.platform.dao.SmsBillLogDao;
import com.sp.platform.dao.SmsBillTempDao;
import com.sp.platform.entity.BillLog;
import com.sp.platform.entity.SmsBillLog;
import com.sp.platform.service.BillLogService;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.vo.BillVo;
import com.sp.platform.vo.SmsBillVo;
import com.yangl.common.hibernate.PaginationSupport;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * User: yangl
 * Date: 13-5-26 下午8:24
 */
@Service
@Transactional
public class BillLogServiceImpl implements BillLogService {

    @Autowired
    private BillLogDao billLogDao;
    @Autowired
    private BillTempDao billTempDao;
    @Autowired
    private PropertyUtils propertyUtils;
    @Autowired
    private SmsBillLogDao smsBillLogDao;
    @Autowired
    private SmsBillTempDao smsBillTempDao;

    @Override
    @Transactional(readOnly = true)
    public BillLog get(int id) {
        return billLogDao.get(id);
    }

    @Override
    public void delete(int id) {
        billLogDao.delete(id);
    }

    @Override
    public void save(BillLog object) {
        billLogDao.save(object);
    }

    @Override
    public List<BillLog> getAll() {
        return billLogDao.getAll();
    }

    @Override
    public boolean isExsits(BillLog billLog) {
        DetachedCriteria dc = DetachedCriteria.forClass(BillLog.class);
        dc.add(Restrictions.eq("caller", billLog.getCaller()));
        dc.add(Restrictions.eq("called", billLog.getCalled()));
        dc.add(Restrictions.eq("btime", billLog.getBtime()));
        List list = billLogDao.findByCriteria(dc);
        if (list != null && list.size() > 0) {
            LogEnum.DEFAULT.info("话单已经记录过，忽略本次{}", billLog);
            return true;
        }
        return false;
    }

    @Override
    public boolean isExsits(String linkid) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<BillLog> getByCaller(String caller) {
        DetachedCriteria dc = DetachedCriteria.forClass(BillLog.class);
        dc.add(Restrictions.eq("caller", caller));
        dc.addOrder(Order.desc("btime"));
        return billLogDao.findByCriteria(dc);
    }

    @Override
    public List<SmsBillLog> getSmsByCaller(String caller) {
        DetachedCriteria dc = DetachedCriteria.forClass(SmsBillLog.class);
        dc.add(Restrictions.eq("mobile", caller));
        dc.addOrder(Order.desc("btime"));
        return billLogDao.findByCriteria(dc);
    }

    @Override
    @Transactional
    public void bufa(int id) {
        BillLog billLog = billLogDao.get(id);
        if (billLog != null) {
            LogEnum.DEFAULT.info("补发{}", billLog);
            billLog.setType(0);

            String syncUrl = CpSyncCache.getSyncUrl(billLog.getCalled());
            if (StringUtils.isNotBlank(syncUrl)) {
                billLog.setSyncurl(syncUrl);
                billTempDao.save(billLog);
                billLogDao.delete(id);
            } else {
                billLogDao.save(billLog);
            }
        }
    }

    @Override
    @Transactional
    public void bufasms(int id) {
        SmsBillLog billLog = smsBillLogDao.get(id);
        if (billLog != null) {
            LogEnum.DEFAULT.info("补发{}", billLog);
            billLog.setType(0);

            String syncUrl = CpSyncCache.getSyncUrl(billLog.getSpnum() + Constants.split_str + billLog.getMsg());
            if (StringUtils.isNotBlank(syncUrl)) {
                billLog.setSyncurl(syncUrl);
                smsBillTempDao.saveBill(billLog);
                smsBillLogDao.delete(id);
            } else {
                smsBillLogDao.save(billLog);
            }
        }
    }

    @Override
    public void deleteRepeat() {
        int count = 0;
        if (propertyUtils.getProperty("db.type").equals("mysql")) {
            count = billLogDao.executeSQL("delete from tbl_bill_log where id in (select id from (select max(id) id from tbl_bill_log where btime > DATE_SUB(now(),INTERVAL 1 DAY) group by caller,called,btime having count(id)>1) t)");
        } else {
            count = billLogDao.executeSQL("delete from tbl_bill_log where id in(select max(id) from tbl_bill_log where btime > (getdate()-1) group by caller,called,btime having count(id)>1)");
        }
    }

    /**
     * 添加未知号段
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveNaHaoduan(String caller) {
        billLogDao.saveNaHaoduan(caller);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillVo> getBillInfo(PageView pageView) {
        return billLogDao.getBillInfo(pageView);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillVo> getCpBillInfo(PageView pageView) {
        return billLogDao.getCpBillInfo(pageView);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SmsBillVo> getSmsBillInfo(PageView pageView) {
        return billLogDao.getSmsBillInfo(pageView);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SmsBillVo> getCpSmsBillInfo(PageView pageView) {
        return billLogDao.getCpSmsBillInfo(pageView);
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        DetachedCriteria dc = DetachedCriteria.forClass(BillLog.class);
        if (pageView.getSpid() > 0) {
            dc.add(Restrictions.eq("spid", pageView.getSpid()));
        }
        if (pageView.getCpid() > 0) {
            dc.add(Restrictions.eq("cpid", pageView.getCpid()));
        }
        if (pageView.getBtime() != null) {
            dc.add(Restrictions.gt("btime", pageView.getBtime()));
        }
        if (pageView.getEtime() != null) {
            dc.add(Restrictions.gt("etime", pageView.getEtime()));
        }
        return billLogDao.findPageByCriteria(dc, page, orders);
    }

    @Override
    public boolean isExsits(SmsBillLog billLog) {
        DetachedCriteria dc = DetachedCriteria.forClass(SmsBillLog.class);
        dc.add(Restrictions.eq("linkid", billLog.getLinkid()));
        List list = billLogDao.findByCriteria(dc);
        if (list != null && list.size() > 0) {
            LogEnum.DEFAULT.info("话单已经记录过，忽略本次{}", billLog);
            return true;
        }
        return false;
    }

    public void save(SmsBillLog billLog) {
        smsBillLogDao.save(billLog);
    }
}
