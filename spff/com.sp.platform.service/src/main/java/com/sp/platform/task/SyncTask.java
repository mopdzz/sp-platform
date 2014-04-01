package com.sp.platform.task;

import com.sp.platform.entity.BillLog;
import com.sp.platform.entity.BillTemp;
import com.sp.platform.service.BillLogService;
import com.sp.platform.service.BillTempService;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.Callable;

/**
 * User: yangl
 * Date: 13-6-8 下午11:45
 */
@Service
@Scope("prototype")
public class SyncTask implements Callable<String> {
    private BillTemp billTemp;

    @Autowired
    private BillLogService billLogService;
    @Autowired
    private BillTempService billTempService;
    @Autowired
    private PropertyUtils propertyUtils;

    @Override
    public String call() {
        long start = System.currentTimeMillis();
        try {
            BillLog billLog = new BillLog();
            billLog.setCaller(billTemp.getCaller());
            billLog.setCalled(billTemp.getCalled());
            billLog.setBtime(billTemp.getBtime());

            if (billLogService.isExsits(billLog)) {
                billTempService.delete(billTemp.getId());
                LogEnum.DEFAULT.info("已经入库，删除临时表并忽略本次同步{}", billTemp);
                return returnFunc(start);
            }

            String url = billTemp.getSyncurl();
            if (StringUtils.isNotBlank(url)) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");
                String linkid = billTemp.getId().toString();

                try {
                    if (propertyUtils.getProperty("db.type").equals("mysql")) {
                        linkid = billTemp.getCaller() + format2.format(billTemp.getBtime());
                    }
                } catch (Exception e) {
                    LogEnum.DEFAULT.info("LinkId gen error {}", e.toString());
                }
                StringBuilder sendBody = new StringBuilder();
                sendBody.append("caller=").append(billTemp.getCaller());
                sendBody.append("&called=").append(billTemp.getCalled());
                sendBody.append("&btime=").append(java.net.URLEncoder.encode(format.format(billTemp.getBtime())));
                sendBody.append("&etime=").append(java.net.URLEncoder.encode(format.format(billTemp.getEtime())));
                sendBody.append("&fee=").append(billTemp.getFee());
                sendBody.append("&linkid=").append(linkid);

                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url + "?" + sendBody.toString());

                HttpResponse response = client.execute(get);
                HttpEntity entity = response.getEntity();
                String returnBody = EntityUtils.toString(entity).trim();
                LogEnum.DEFAULT.info("同步一条话单{}，linkid{}，返回[{}]", billTemp, linkid, returnBody);
                if ("1".equals(returnBody)) {
                    billLog.setBtime(billTemp.getBtime());
                    billLog.setEtime(billTemp.getEtime());
                    billLog.setTime(billTemp.getTime());
                    billLog.setFee(billTemp.getFee());
                    billLog.setProvince(billTemp.getProvince());
                    billLog.setCity(billTemp.getCity());
                    billLog.setSfid(billTemp.getSfid());
                    billLog.setCpid(billTemp.getCpid());
                    billLog.setSyncurl(billTemp.getSyncurl());
                    billLog.setParentid(billTemp.getParentid());
                    billLogService.save(billLog);
                    billTempService.delete(billTemp.getId());
                    return returnFunc(start);
                }
            }
        } catch (IOException e) {
            LogEnum.DEFAULT.error("同步出现异常{}", billTemp, e);
        }
        LogEnum.DEFAULT.error("同步失败，计数加1,{}", billTemp);
        billTempService.addSendNum(billTemp.getId());

        return returnFunc(start);
    }

    private String returnFunc(long start) {
        return billTemp.getCaller() + "-" + billTemp.getCalled() + "同步耗时：" + (System.currentTimeMillis() - start);
    }

    public BillTemp getBillTemp() {
        return billTemp;
    }

    public void setBillTemp(BillTemp billTemp) {
        this.billTemp = billTemp;
    }
}
