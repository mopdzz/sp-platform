package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.BillTempDao;
import com.sp.platform.dao.SmsBillTempDao;
import com.sp.platform.entity.BillLog;
import com.sp.platform.entity.BillTemp;
import com.sp.platform.entity.SmsBillLog;
import com.sp.platform.entity.SmsBillTemp;
import com.sp.platform.service.BillTempService;
import com.sp.platform.util.LogEnum;
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
 * Date: 13-5-26 下午8:34
 */
@Service
@Transactional
public class BillTempServiceImpl implements BillTempService {

    @Autowired
    private BillTempDao billTempDao;
    @Autowired
    private SmsBillTempDao smsBillTempDao;

    @Override
    public BillTemp get(int id) {
        return billTempDao.get(id);
    }

    @Override
    public void delete(int id) {
        billTempDao.delete(id);
    }

    @Override
    public void save(BillTemp object) {
        billTempDao.save(object);
    }

    @Override
    public List<BillTemp> getAll() {
        return billTempDao.getAll();
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        return null;
    }

    @Override
    public void save(BillLog billLog) {
        billTempDao.save(billLog);
    }

    @Override
    public List<BillTemp> getSyncBill() {
        DetachedCriteria dc = DetachedCriteria.forClass(BillTemp.class);
        dc.add(Restrictions.lt("sendnum", 3));

//        PaginationSupport page = new PaginationSupport(100);
//        page = billTempDao.findPageByCriteria(dc, page, new Order[]{Order.asc("id")});
//        return page.getItems();

        return billTempDao.findByCriteria(dc);
    }

    @Override
    public void addSendNum(int id) {
        String sql = "update tbl_bill_temp set sendnum=sendnum+1 where id=" + id;
        billTempDao.executeSQL(sql);
    }

    @Override
    public List<BillTemp> getByCaller(String caller) {
        DetachedCriteria dc = DetachedCriteria.forClass(BillTemp.class);
        dc.add(Restrictions.eq("caller", caller));
        dc.addOrder(Order.desc("btime"));
        return billTempDao.findByCriteria(dc);
    }

    @Override
    public List<SmsBillTemp> getSmsByCaller(String caller) {
        DetachedCriteria dc = DetachedCriteria.forClass(SmsBillTemp.class);
        dc.add(Restrictions.eq("mobile", caller));
        dc.addOrder(Order.desc("btime"));
        return billTempDao.findByCriteria(dc);
    }

    @Override
    public void sync(int id) {
        BillTemp billTemp = billTempDao.get(id);
        if (billTemp != null) {
            LogEnum.DEFAULT.info("重新同步{}", billTemp);
            billTemp.setSendnum(0);
            billTempDao.save(billTemp);
        }
    }
    @Override
    public void syncsms(int id) {
        SmsBillTemp billTemp = smsBillTempDao.get(id);
        if (billTemp != null) {
            LogEnum.DEFAULT.info("重新同步{}", billTemp);
            billTemp.setSendnum(0);
            smsBillTempDao.save(billTemp);
        }
    }

    @Override
    public SmsBillTemp getByLinkid(String linkid) {
        DetachedCriteria dc = DetachedCriteria.forClass(SmsBillTemp.class);
        dc.add(Restrictions.eq("linkid", linkid));
        List<SmsBillTemp> list = smsBillTempDao.findByCriteria(dc);
        if(list != null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public void save(SmsBillTemp smsBillTemp) {
        smsBillTempDao.save(smsBillTemp);
    }

    @Override
    public void saveMr(SmsBillLog billLog) {
        smsBillTempDao.saveMr(billLog);
    }

    @Override
    public void saveMo(SmsBillLog billLog) {
        smsBillTempDao.saveMo(billLog);
    }

    public void saveBill(SmsBillLog billLog) {
        smsBillTempDao.saveBill(billLog);
    }

    @Override
    public List<SmsBillTemp> getSyncSmsBill() {
        DetachedCriteria dc = DetachedCriteria.forClass(SmsBillTemp.class);
        dc.add(Restrictions.lt("sendnum", 3));
        dc.add(Restrictions.eq("flag", 4));
        return smsBillTempDao.findByCriteria(dc);
    }

    @Override
    public void addSmsSendNum(int id) {
        String sql = "update sms_bill_temp set sendnum=sendnum+1 where id=" + id;
        billTempDao.executeSQL(sql);
    }

    @Override
    public void deleteSmsTemp(Integer id) {
        smsBillTempDao.delete(id);
    }
}
