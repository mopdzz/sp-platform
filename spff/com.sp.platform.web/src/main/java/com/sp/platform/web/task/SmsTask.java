package com.sp.platform.web.task;

import com.sp.platform.cache.CpSyncCache;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.cache.ProvReduceCache;
import com.sp.platform.cache.SnumCache;
import com.sp.platform.common.Constants;
import com.sp.platform.entity.*;
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

import java.util.Date;

/**
 * User: yangl
 * Date: 13-5-26 上午1:04
 */
@Service
@Scope("prototype")
public class SmsTask implements Runnable {
    private String mobile;
    private String spnum;
    private String msg;
    private String linkid;
    private String status;

    @Autowired
    private CacheCheckUser cacheCheckUser;
    @Autowired
    private BillLogService billLogService;
    @Autowired
    private BillTempService billTempService;

    private int fee;
    private String key;
    private String key2;
    private SmsBillLog billLog;

    public SmsTask() {
    }

    public SmsTask(String mobile, String spnum, String msg, String linkid, String status) {
        this.mobile = mobile;
        this.spnum = spnum;
        this.msg = msg;
        this.linkid = linkid;
        this.status = status;
    }

    @Override
    public void run() {
        StopWatch stopWatch = new StopWatch();
        boolean flag = true;
        try {
            stopWatch.start();
            String cacheKey = spnum + Constants.split_str + msg;

            ServiceNum snum = SnumCache.get(cacheKey);
            int fee = snum == null ? 100 : snum.getFee();

            if (fee < 0 || fee > 1000) {
                LogEnum.BILL_ERROR.error("{}, {}, {}, {}, {}", mobile, spnum, msg, linkid, status);
                return;
            }

            billLog = new SmsBillLog(mobile, spnum, msg, linkid, status);
            if (billLogService.isExsits(billLog)) {
                LogEnum.DEFAULT.info("已经入库，忽略本次同步{}", billLog);
                return;
            }

            if (StringUtils.isBlank(billLog.getSpnum())) {
                saveMr(billLog);
                return;
            }

            billLog.setProvince(HaoduanCache.getProvince(mobile));
            billLog.setCity(HaoduanCache.getCity(mobile));
            billLog.setFee(fee);
            billLog.setSfid(SnumCache.getSpid(cacheKey));
            billLog.setCpid(CpSyncCache.getCpId(cacheKey));
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
            LogEnum.DEFAULT.info("接收话单[{}]，执行结果: {}, 耗时----：{}", this, flag, stopWatch.getTime());
        }
    }

    //屏蔽地市管理
    private boolean blackArea(SmsBillLog billLog, int fee) {
        CpNum cpNum = CpSyncCache.getCp(spnum + Constants.split_str + msg);
        if (cpNum == null) {
            return true;
        }
        String memo = cpNum.getBlackinfo();
        if (StringUtils.isBlank(memo)) {
            ServiceNum serviceNum = SnumCache.get(spnum + Constants.split_str + msg);
            if (serviceNum != null) {
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
            if (temp.length > 1 && StringUtils.isNotBlank(temp[1])
                    && temp[1].indexOf(billLog.getProvince()) >= 0) {
                LogEnum.DEFAULT.info(new StringBuilder(billLog.toString()).
                        append("---- 在黑名单省份  ").append(temp[1]).toString());
                saveBill(billLog, fee, true);
                return false;
            }
            // 黑名单地市
            if (temp.length > 2 && StringUtils.isNotBlank(temp[2])
                    && temp[2].indexOf(billLog.getProvince()) >= 0) {
                LogEnum.DEFAULT.info(new StringBuilder(billLog.toString()).
                        append("---- 黑名单地市 ").append(temp[2]).toString());
                saveBill(billLog, fee, true);
                return false;
            }
        }
        return true;
    }

    private boolean callerLimit(SmsBillLog billLog, int fee) {
        //----------------------------- 用户日上限 -----------------------

        // 长号码_指令
        String tempKey = spnum + Constants.split_str + msg;

        //取日上限, 设置都在通道号码类中
        int limitFee = SnumCache.getDayLimit(tempKey);
        if (limitFee <= 0) { //没有日上限
            LogEnum.DEFAULT.info(new StringBuilder(billLog.toString()).
                    append("---- 没有设置日上限").toString());
            return true;
        }

        int tempFee = cacheCheckUser.getCallerDayFee(mobile + Constants.split_str + tempKey);

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
        limitFee = SnumCache.getMonthLimit(tempKey);
        if (limitFee <= 0) { //分省没有月上限
            LogEnum.DEFAULT.info(new StringBuilder(billLog.toString()).
                    append("---- 没有设置日上限").toString());
            return true;
        }

        tempFee = cacheCheckUser.getCallerMonthFee(mobile + Constants.split_str + tempKey);
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

    private boolean calledLimit(SmsBillLog billLog, int fee) {
        //----------------------------- 号码日上限 -----------------------
        key = spnum + Constants.split_str + msg + Constants.split_str + billLog.getProvince();

        // 长号码_指令
        String tempKey = spnum + Constants.split_str + msg;

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
            limitFee = CpSyncCache.getDayLimit(tempKey);
            userFee = cacheCheckUser.getCalledDayFee(tempKey);
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
            limitFee = CpSyncCache.getMonthLimit(tempKey);
            userFee = cacheCheckUser.getCalledMonthFee(tempKey);
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

    private boolean reduce(SmsBillLog billLog, int fee) {
        //----------------------------- 扣量 -----------------------------


        key2 = " 没有扣量...";
        // 普通通道合作的扣量判断
        key = spnum + Constants.split_str + msg + Constants.split_str + billLog.getProvince();

        boolean flag = false;
        //首先判断分省是否有设置扣量
        int reduce = ProvReduceCache.getProvReduce(key);
        if (reduce <= 0) {
            flag = true;
            //如果分省没有扣量，查看是否长号码设有扣量
            key = spnum + Constants.split_str + msg;
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
            if (flag) {
                calledDayCount = cacheCheckUser.getCalledDayCount(key);
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

    private void saveMr(SmsBillLog billLog) {
        SmsBillTemp smsBillTemp = billTempService.getByLinkid(billLog.getLinkid());
        if (smsBillTemp == null) {
            billTempService.saveMr(billLog);
        } else {
            if (smsBillTemp.getFlag() == 4 || smsBillTemp.getFlag() == 2) {
                return;
            } else {
                smsBillTemp.setStatus(billLog.getStatus());
                smsBillTemp.setEtime(new Date());
                smsBillTemp.setFlag(4);
                billTempService.save(smsBillTemp);
            }
        }
    }

    private void saveMo(SmsBillLog billLog, SmsBillTemp smsBillTemp, boolean flag, int fee) {
        if (smsBillTemp == null) {
            // 长号码_指令
            String tempKey = spnum + Constants.split_str + msg;

            //缓存记录用户费用、长号费用
            cacheCheckUser.addCalledFee(tempKey, fee, flag);
            cacheCheckUser.addCalledProvinceFee(tempKey + Constants.split_str + billLog.getProvince(), fee, flag);
            cacheCheckUser.addCallerFee(mobile + Constants.split_str + tempKey, fee);

            billTempService.saveMo(billLog);
        } else {
            if (smsBillTemp.getFlag() == 4 || smsBillTemp.getFlag() == 1) {
                return;
            } else {
                // 长号码_指令
                String tempKey = spnum + Constants.split_str + msg;

                //缓存记录用户费用、长号费用
                cacheCheckUser.addCalledFee(tempKey, fee, flag);
                cacheCheckUser.addCalledProvinceFee(tempKey + Constants.split_str + billLog.getProvince(), fee, flag);
                cacheCheckUser.addCallerFee(mobile + Constants.split_str + tempKey, fee);

                smsBillTemp.setMobile(billLog.getMobile());
                smsBillTemp.setType(billLog.getType());
                smsBillTemp.setSpnum(billLog.getSpnum());
                smsBillTemp.setMsg(billLog.getMsg());
                smsBillTemp.setBtime(new Date());
                smsBillTemp.setProvince(billLog.getProvince());
                smsBillTemp.setCity(billLog.getCity());
                smsBillTemp.setFee(billLog.getFee());
                smsBillTemp.setSfid(billLog.getSfid());
                smsBillTemp.setCpid(billLog.getCpid());
                smsBillTemp.setFlag(4);
                smsBillTemp.setSyncurl(billLog.getSyncurl());
                smsBillTemp.setParentid(billLog.getParentid());
                billTempService.save(smsBillTemp);
            }
        }
    }

    private void saveBill(SmsBillLog billLog, int fee, boolean flag) {
        SmsBillTemp smsBillTemp = billTempService.getByLinkid(billLog.getLinkid());
        if (smsBillTemp != null && smsBillTemp.getFlag() == 4) {
            return;
        }

        String temp = flag == true ? "扣量" : "普通";

        if (flag) { // 扣量
            billLog.setType(1);
        } else {
            String syncUrl = CpSyncCache.getSyncUrl(spnum + Constants.split_str + msg);
            if (StringUtils.isNotBlank(syncUrl)) {
                billLog.setSyncurl(syncUrl);
                temp += "同步";
            }
        }
        if (StringUtils.isBlank(billLog.getStatus())) {
            saveMo(billLog, smsBillTemp, flag, fee);
        } else {
            if (smsBillTemp != null) {
                saveMr(billLog);
            } else {
                // 长号码_指令
                String tempKey = spnum + Constants.split_str + msg;

                //缓存记录用户费用、长号费用
                cacheCheckUser.addCalledFee(tempKey, fee, flag);
                cacheCheckUser.addCalledProvinceFee(tempKey + Constants.split_str + billLog.getProvince(), fee, flag);
                cacheCheckUser.addCallerFee(mobile + Constants.split_str + tempKey, fee);

                billTempService.saveBill(billLog);
            }
        }

        if (HaoduanCache.NA.equals(billLog.getProvince())) {
            billLogService.saveNaHaoduan(mobile);
            LogEnum.DEFAULT.info("保存未知号码[{}]", mobile);
        }
        LogEnum.DEFAULT.info("保存{}话单 mobile:{}, spnum:{}, msg:{}, linkid:{}, status:{}", temp, mobile, spnum, msg, linkid, status);
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSpnum() {
        return spnum;
    }

    public void setSpnum(String spnum) {
        this.spnum = spnum;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SmsTask{" +
                "mobile='" + mobile + '\'' +
                ", spnum='" + spnum + '\'' +
                ", msg='" + msg + '\'' +
                ", linkid='" + linkid + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
