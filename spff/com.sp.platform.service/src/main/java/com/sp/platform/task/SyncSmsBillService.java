package com.sp.platform.task;

import com.sp.platform.entity.BillTemp;
import com.sp.platform.entity.SmsBillTemp;
import com.sp.platform.service.BillLogService;
import com.sp.platform.service.BillTempService;
import com.sp.platform.util.AppContextHolder;
import com.sp.platform.util.LogEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * User: yangl
 * Date: 13-6-8 下午11:19
 */
@Service
public class SyncSmsBillService {

    @Autowired
    private BillTempService billTempService;
    @Autowired
    private BillLogService billLogService;


    ThreadPoolExecutor threadPool2 = new ThreadPoolExecutor(10, 20, 30,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(30),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public void syncBill() {
        billLogService.deleteRepeat();

        List<SmsBillTemp> list = billTempService.getSyncSmsBill();
        if (list != null && list.size() > 0) {
            int i = 0;
            List<SyncSmsTask> temp = new ArrayList<SyncSmsTask>();
            for (SmsBillTemp billTemp : list) {
                i++;
                SyncSmsTask syncTask = (SyncSmsTask) AppContextHolder.getContext().getBean("syncSmsTask");
                syncTask.setSmsBillTemp(billTemp);
                temp.add(syncTask);

                if (i >= 10) {
                    syncForUrl(temp);
                    temp.clear();
                    i = 0;
                }
            }
            if (temp.size() > 0) {
                syncForUrl(temp);
            }
        }
    }

    private void syncForUrl(List<SyncSmsTask> temp) {
        try {
            List<Future<String>> futures = threadPool2.invokeAll(temp);
            for (Future<String> future : futures) {
                String str = future.get();
                LogEnum.DEFAULT.info(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
