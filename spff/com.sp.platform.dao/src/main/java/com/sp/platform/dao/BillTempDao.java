package com.sp.platform.dao;

import com.sp.platform.entity.BillLog;
import com.sp.platform.entity.BillTemp;
import com.yangl.common.hibernate.HibernateDaoUtil;
import org.springframework.stereotype.Repository;

/**
 * User: yangl
 * Date: 13-5-26 下午4:47
 */
@Repository
public class BillTempDao extends HibernateDaoUtil<BillTemp, Integer> {

    private static final String SAVE_BILL_TEMP = "insert into tbl_bill_temp(caller,called,btime,etime,time,fee,sfid," +
            "cpid,sendnum,province,city,syncurl,parentid) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public void save(BillLog billLog) {
        getSession().createSQLQuery(SAVE_BILL_TEMP)
                .setParameter(0, billLog.getCaller())
                .setParameter(1, billLog.getCalled())
                .setParameter(2, billLog.getBtime())
                .setParameter(3, billLog.getEtime())
                .setParameter(4, billLog.getTime())
                .setParameter(5, billLog.getFee())
                .setParameter(6, billLog.getSfid())
                .setParameter(7, billLog.getCpid())
                .setParameter(8, 0)
                .setParameter(9, billLog.getProvince())
                .setParameter(10, billLog.getCity())
                .setParameter(11, billLog.getSyncurl())
                .setParameter(12, billLog.getParentid())
                .executeUpdate();
    }
}
