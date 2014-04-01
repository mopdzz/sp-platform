package com.sp.platform.web.task;

import com.sp.platform.entity.MonthData;
import com.sp.platform.service.MonthDataService;
import com.sp.platform.util.LogEnum;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-2-13
 * Time: 下午9:42
 * To change this template use File | Settings | File Templates.
 */
@Service
@Scope("prototype")
public class MonthTask implements Runnable {
    @Resource
    private MonthDataService monthDataService;

    MonthData monthData;

    @Override
    public void run() {
        StopWatch stopWatch = new StopWatch();
        boolean flag = true;
        try {
            stopWatch.start();
            if (monthData.getStatus() != null && monthData.getStatus().indexOf("成功") >= 0) {
                monthData.setType(0);
            } else {
                monthData.setType(1);
            }
            Date now = new Date();
            try {
                monthData.setCtime(new SimpleDateFormat("yyyyMMddHHmmss").parse(monthData.getRequesttime()));
            } catch (Exception e) {
                monthData.setCtime(now);
            }
            monthData.setUtime(now);
            monthDataService.save(monthData);
        } catch (Exception e) {
            flag = false;
            LogEnum.DEFAULT.error(e.toString());
            e.printStackTrace();
        } finally {
            stopWatch.stop();
            LogEnum.DEFAULT.info("接收话单[{}]，执行结果: {}, 耗时----：{}", this, flag, stopWatch.getTime());
        }
    }

    public void setMonthData(MonthData monthData) {
        this.monthData = monthData;
    }
}
