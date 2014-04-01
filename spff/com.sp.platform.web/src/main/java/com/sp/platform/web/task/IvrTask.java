package com.sp.platform.web.task;

import com.sp.platform.cache.CpSyncCache;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.cache.ProvReduceCache;
import com.sp.platform.cache.SnumCache;
import com.sp.platform.common.Constants;
import com.sp.platform.entity.BillLog;
import com.sp.platform.entity.CpNum;
import com.sp.platform.entity.ProvReduce;
import com.sp.platform.entity.ServiceNum;
import com.sp.platform.service.BillLogService;
import com.sp.platform.service.BillTempService;
import com.sp.platform.util.CacheCheckUser;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.ReduceAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: yangl
 * Date: 13-5-26 上午1:04
 */
@Service
@Scope("prototype")
public class IvrTask implements Runnable {
    private String caller;
    private String called;
    private String btime;
    private String etime;
    private String format;

    @Autowired
    private CacheCheckUser cacheCheckUser;
    @Autowired
    private BillLogService billLogService;
    @Autowired
    private BillTempService billTempService;

    private int fee;
    private String key;
    private String key2;
    private BillLog billLog;

    public IvrTask() {
    }

    public IvrTask(String caller, String called, String btime, String etime) {
        this.caller = caller;
        this.called = called;
        this.btime = btime;
        this.etime = etime;
    }

    @Override
    public void run() {
        StopWatch stopWatch = new StopWatch();
        boolean flag = true;
        try {
            stopWatch.start();

            DateFormat dateFormat = new SimpleDateFormat(format);
            Date t = dateFormat.parse(btime);
            Date e = dateFormat.parse(etime);
            long diff = e.getTime() - t.getTime();
            int s = (int) (diff / 1000L);
            ServiceNum snum = SnumCache.get(called);
            int calledFee = snum == null ? 100 : snum.getFee();

            int fee = genFee(s, calledFee);
            if (fee < 0 || fee > 6000) {
                LogEnum.BILL_ERROR.error("{}, {}, {}, {}", caller, called, btime, etime);
                return;
            }


            billLog = new BillLog(caller, called, t, e);
            billLog.setProvince(HaoduanCache.getProvince(caller));
            billLog.setCity(HaoduanCache.getCity(caller));
            billLog.setTime(s);
            billLog.setFee(fee);
            billLog.setSfid(SnumCache.getSpid(called));
            billLog.setCpid(CpSyncCache.getCpId(called));
            int parentId = CpSyncCache.getParentId(billLog.getCpid());
            if (parentId > 0) {
                billLog.setParentid(parentId);
            } else {
                billLog.setParentid(billLog.getCpid());
            }

            //屏蔽地市
            if (!blackArea(billLog, fee)) {
                //被扣量
                return;
            }

            //用户上限
            if (!callerLimit(billLog, fee)) {
                //被扣量
                return;
            }

            //号码上限
            if (!calledLimit(billLog, fee)) {
                //被扣量
                return;
            }

            //扣量
            if (!reduce(billLog, fee)) {
                //被扣量
                return;
            }

            saveBill(billLog, fee, false);
        } catch (Exception e) {
            flag = false;
            LogEnum.DEFAULT.error(e.toString());
            e.printStackTrace();
        } finally {
            stopWatch.stop();
            long time = stopWatch.getTime();
            LogEnum.DEFAULT.info("接收话单[{}]，执行结果: {}, 耗时----：{}", this, flag, stopWatch.getTime());
        }
    }

    //屏蔽地市管理
    private boolean blackArea(BillLog billLog, int fee) {
        CpNum cpNum = CpSyncCache.getCp(called);
        if (cpNum == null) {
            return true;
        }
        String memo = cpNum.getBlackinfo();
        if(StringUtils.isBlank(memo)){
            ServiceNum serviceNum = SnumCache.get(called);
            if(serviceNum != null){
                memo = serviceNum.getMemo();
            }
        }

        if (StringUtils.isNotBlank(memo)) {
            String[] temp = memo.split(";");

            // 白名单省份
            if (StringUtils.isNotBlank(temp[0])) {
                if (temp[0].indexOf(billLog.getProvince()) < 0) {
                    LogEnum.DEFAULT.info(new StringBuilder(billLog.toString()).
                            append("---- 不在白名单省份 ").append(temp[0]).toString());
                    saveBill(billLog, fee, true);
                    return false;
                }
            }
            // 黑名单省份
            if (StringUtils.isNotBlank(temp[1])
                    && temp[1].indexOf(billLog.getProvince()) >= 0) {
                LogEnum.DEFAULT.info(new StringBuilder(billLog.toString()).
                        append("---- 在黑名单省份 ").append(temp[1]).toString());
                saveBill(billLog, fee, true);
                return false;
            }
            // 黑名单地市
            if (StringUtils.isNotBlank(temp[2])
                    && temp[2].indexOf(billLog.getProvince()) >= 0) {
                LogEnum.DEFAULT.info(new StringBuilder(billLog.toString()).
                        append("---- 黑名单地市 ").append(temp[2]).toString());
                saveBill(billLog, fee, true);
                return false;
            }
        }
        return true;
    }

    private boolean callerLimit(BillLog billLog, int fee) {
        //----------------------------- 用户日上限 -----------------------
        //取日上限, 设置都在通道号码类中
        int limitFee = SnumCache.getDayLimit(called);
        if (limitFee <= 0) { //没有日上限
            LogEnum.DEFAULT.info(new StringBuilder(billLog.toString()).
                    append("---- 没有设置日上限").toString());
            return true;
        }

        int tempFee = cacheCheckUser.getCallerMonthFee(caller + Constants.split_str + called);

        if (limitFee > 0 && tempFee > limitFee) {
            LogEnum.DEFAULT.info(new StringBuilder(billLog.toString()).
                    append("---- 超用户日上限 ").append(limitFee)
                    .append(",日费用：")
                    .append(tempFee).toString());
            saveBill(billLog, fee, true);
            return false;
        }

        //----------------------------- 用户月上限 -----------------------
        //取月上限, 设置都在通道号码类中
        limitFee = SnumCache.getMonthLimit(called);
        if (limitFee <= 0) { //分省没有月上限
            LogEnum.DEFAULT.info(new StringBuilder(billLog.toString()).
                    append("---- 没有设置日上限").toString());
            return true;
        }

        tempFee = cacheCheckUser.getCallerMonthFee(caller + Constants.split_str + called);
        //如果没有设置上限， 或者费用未达到上限，继续
        if (limitFee > 0 && tempFee > limitFee) {
            LogEnum.DEFAULT.info(new StringBuilder(billLog.toString()).
                    append("---- 超用户月上限 ").append(limitFee)
                    .append(",月费用：")
                    .append(tempFee).toString());
            saveBill(billLog, fee, true);
            return false;
        }
        //----------------------------- 用户上限 -----------------------
        return true;
    }

    private boolean calledLimit(BillLog billLog, int fee) {
        //----------------------------- 号码日上限 -----------------------
        key = called + "_" + billLog.getProvince();
        boolean proFlag = false;
        int limitFee = 0;
        int userFee = 0;
        ProvReduce provReduce = ProvReduceCache.get(key);
        if (provReduce != null && provReduce.getDaylimit() > 0) {
            proFlag = true;
            limitFee = provReduce.getDaylimit();
            userFee = cacheCheckUser.getCalledProvinceDayFee(key);
            key2 = "分省日上限: ";
        } else {
            limitFee = CpSyncCache.getDayLimit(called);
            userFee = cacheCheckUser.getCalledDayFee(called);
            key2 = "默认日上限: ";
        }
        if (limitFee > 0 && userFee > limitFee) {
            LogEnum.DEFAULT.info(new StringBuilder(billLog.toString()).
                    append("---- 超号码").append(key2).append(limitFee)
                    .append(",日费用：")
                    .append(userFee).toString());
            saveBill(billLog, fee, true);
            return false;
        }

        //----------------------------- 号码月上限 -----------------------
        if (provReduce != null && provReduce.getMonthlimit() > 0) {
            limitFee = provReduce.getMonthlimit();
            userFee = cacheCheckUser.getCalledProvinceMonthFee(key);
            key2 = "分省月上限: ";
        } else {
            limitFee = CpSyncCache.getMonthLimit(called);
            userFee = cacheCheckUser.getCalledMonthFee(called);
            key2 = "默认月上限: ";
        }
        //如果没有设置上限， 或者费用未达到上限，继续
        if (limitFee > 0 && userFee > limitFee) {
            LogEnum.DEFAULT.info(new StringBuilder(billLog.toString()).
                    append("---- 超号码").append(key2).append(limitFee)
                    .append(",月费用：")
                    .append(userFee).toString());
            saveBill(billLog, fee, true);
            return false;
        }
        //----------------------------- 号码上限 -----------------------
        return true;
    }

    private boolean reduce(BillLog billLog, int fee) {
        //----------------------------- 扣量 -----------------------------
        key2 = " 没有扣量...";
        // 普通通道合作的扣量判断
        key = called + "_" + billLog.getProvince();

        //首先判断分省是否有设置扣量
        int reduce = ProvReduceCache.getProvReduce(key);
        if (reduce <= 0) {
            //如果分省没有扣量，查看是否长号码设有扣量
            key = called;
            CpNum cpNum = CpSyncCache.getCp(key);
            if (cpNum != null) {
                reduce = cpNum.getReduce();
                key2 = " 被合作方默认设置扣量...";
            }
        } else {
            key2 = " 被合作方省份设置扣量...";
        }

        if (reduce > 0) {
            int calledDayCount;
            if (key.equals(called)) {
                calledDayCount = cacheCheckUser.getCalledDayCount(called);
            } else {
                calledDayCount = cacheCheckUser.getCalledProvinceDayCount(key);
            }
            if (ReduceAlgorithm.isReduce(key, reduce, calledDayCount)) {
                LogEnum.DEFAULT.info(new StringBuilder(billLog.toString()).
                        append("---- 扣量信息 key=").append(key)
                        .append(",扣量比例：").append(reduce)
                        .append("%， 访问数为：").append(calledDayCount)
                        .append(key2).toString());

                saveBill(billLog, fee, true);
                return false;
            }
        }
        //----------------------------- 扣量 -----------------------------
        return true;
    }

    private void saveBill(BillLog billLog, int fee, boolean flag) {
        if (billLogService.isExsits(billLog)) {
            return;
        }
        String temp = flag == true ? "扣量" : "普通";

        //缓存记录用户费用、长号费用
        cacheCheckUser.addCalledFee(called, fee, flag);
        cacheCheckUser.addCalledProvinceFee(called + "_" + billLog.getProvince(), fee, flag);
        cacheCheckUser.addCallerFee(caller + Constants.split_str + called, fee);
        if (flag) { // 扣量
            billLog.setType(1);
            billLogService.save(billLog);
        } else {
            String syncUrl = CpSyncCache.getSyncUrl(called);
            if (StringUtils.isNotBlank(syncUrl)) {
                billLog.setSyncurl(syncUrl);
                billTempService.save(billLog);
                temp += "同步";
            } else {
                billLogService.save(billLog);
            }
        }

        if (HaoduanCache.NA.equals(billLog.getProvince())) {
            billLogService.saveNaHaoduan(caller);
            LogEnum.DEFAULT.info("保存未知号码[{}]", caller);
        }
        LogEnum.DEFAULT.info("保存{}话单 caller{}, called{}, btime{}, etime{}, fee{}", temp, caller, called, btime, etime, fee);
    }

    private int genFee(int senc, int calledFee) {
        int fee = (senc / 60 + (senc % 60 == 0 ? 0 : 1)) * calledFee;
        // 防止程序算错，重新计算一次
        if (fee < 0 || fee > 60) {
            fee = (senc / 60 + (senc % 60 == 0 ? 0 : 1)) * calledFee;
        }
        return fee;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCalled() {
        return called;
    }

    public void setCalled(String called) {
        this.called = called;
    }

    public String getBtime() {
        return btime;
    }

    public void setBtime(String btime) {
        this.btime = btime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return "IvrTask{" +
                "format='" + format + '\'' +
                ", caller='" + caller + '\'' +
                ", called='" + called + '\'' +
                ", btime='" + btime + '\'' +
                ", etime='" + etime + '\'' +
                '}';
    }
}
