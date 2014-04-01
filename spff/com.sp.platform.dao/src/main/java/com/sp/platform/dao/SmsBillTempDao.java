package com.sp.platform.dao;

import com.sp.platform.entity.BillLog;
import com.sp.platform.entity.SmsBillLog;
import com.sp.platform.entity.SmsBillTemp;
import com.yangl.common.hibernate.HibernateDaoUtil;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * User: yangl
 * Date: 13-7-13 下午10:25
 */
@Repository
public class SmsBillTempDao extends HibernateDaoUtil<SmsBillTemp, Integer> {

    private static final String SAVE_BILL_TEMP_MR = "insert into sms_bill_temp(mobile,linkid,status,flag,etime,sendnum,fee,sfid,cpid,parentid,type) values(?,?,?,?,?,?,0,0,0,0,0)";

    private static final String SAVE_BILL_TEMP_MT = "insert into sms_bill_temp(mobile,spnum,msg,linkid,btime,province,city," +
            "fee,sfid,cpid,flag,sendnum,syncurl,parentid,type) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private static final String SAVE_BILL_TEMP_BILL = "insert into sms_bill_temp(mobile,spnum,msg,linkid,btime,province,city," +
            "fee,sfid,cpid,flag,sendnum,syncurl,parentid,type,status,etime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public void saveMr(SmsBillLog billLog) {
        getSession().createSQLQuery(SAVE_BILL_TEMP_MR)
                .setParameter(0, billLog.getMobile())
                .setParameter(1, billLog.getLinkid())
                .setParameter(2, billLog.getStatus())
                .setParameter(3, 2)
                .setParameter(4, new Date())
                .setParameter(5, 0)
                .executeUpdate();
    }

    public void saveMo(SmsBillLog billLog) {
        getSession().createSQLQuery(SAVE_BILL_TEMP_MT)
                .setParameter(0, billLog.getMobile())
                .setParameter(1, billLog.getSpnum())
                .setParameter(2, billLog.getMsg())
                .setParameter(3, billLog.getLinkid())
                .setParameter(4, new Date())
                .setParameter(5, billLog.getProvince())
                .setParameter(6, billLog.getCity())
                .setParameter(7, billLog.getFee())
                .setParameter(8, billLog.getSfid())
                .setParameter(9, billLog.getCpid())
                .setParameter(10, 1)
                .setParameter(11, 0)
                .setParameter(12, billLog.getSyncurl())
                .setParameter(13, billLog.getParentid())
                .setParameter(14, billLog.getType())
                .executeUpdate();
    }


    public void saveBill(SmsBillLog billLog) {
        Date date = new Date();
        getSession().createSQLQuery(SAVE_BILL_TEMP_BILL)
                .setParameter(0, billLog.getMobile())
                .setParameter(1, billLog.getSpnum())
                .setParameter(2, billLog.getMsg())
                .setParameter(3, billLog.getLinkid())
                .setParameter(4, date)
                .setParameter(5, billLog.getProvince())
                .setParameter(6, billLog.getCity())
                .setParameter(7, billLog.getFee())
                .setParameter(8, billLog.getSfid())
                .setParameter(9, billLog.getCpid())
                .setParameter(10, 4)
                .setParameter(11, 0)
                .setParameter(12, billLog.getSyncurl())
                .setParameter(13, billLog.getParentid())
                .setParameter(14, billLog.getType())
                .setParameter(15, billLog.getStatus())
                .setParameter(16, date)
                .executeUpdate();
    }
}
