package com.sp.platform.task;

import com.sp.platform.entity.SmsBillLog;
import com.sp.platform.entity.SmsBillTemp;
import com.sp.platform.service.BillLogService;
import com.sp.platform.service.BillTempService;
import com.sp.platform.util.LogEnum;
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
import java.net.URLEncoder;
import java.util.concurrent.Callable;

/**
 * User: yangl
 * Date: 13-6-8 下午11:45
 */
@Service
@Scope("prototype")
public class SyncSmsTask implements Callable<String> {
    private SmsBillTemp smsBillTemp;

    @Autowired
    private BillLogService billLogService;
    @Autowired
    private BillTempService billTempService;

    @Override
    public String call() {
        long start = System.currentTimeMillis();

        try {
            String url = smsBillTemp.getSyncurl();
            if (!StringUtils.equals("DELIVRD", StringUtils.upperCase(smsBillTemp.getStatus()))) {
                smsBillTemp.setFee(0);
            }

            SmsBillLog billLog = new SmsBillLog();
            billLog.setLinkid(smsBillTemp.getLinkid());
            billLog.setMobile(smsBillTemp.getMobile());
            billLog.setSpnum(smsBillTemp.getSpnum());
            billLog.setMsg(smsBillTemp.getMsg());
            billLog.setStatus(smsBillTemp.getStatus());
            billLog.setBtime(smsBillTemp.getBtime());
            billLog.setEtime(smsBillTemp.getEtime());
            billLog.setProvince(smsBillTemp.getProvince());
            billLog.setCity(smsBillTemp.getCity());
            billLog.setFee(smsBillTemp.getFee());
            billLog.setSfid(smsBillTemp.getSfid());
            billLog.setCpid(smsBillTemp.getCpid());
            billLog.setParentid(smsBillTemp.getParentid());
            billLog.setType(smsBillTemp.getType());
            billLog.setSyncurl(smsBillTemp.getSyncurl());

            if (billLogService.isExsits(billLog)) {
                billTempService.deleteSmsTemp(smsBillTemp.getId());
                LogEnum.DEFAULT.info("已经入库，删除临时表并忽略本次同步{}", smsBillTemp);
                return returnFunc(start);
            }

            if (StringUtils.isNotBlank(url)) {
                StringBuilder sendBody = new StringBuilder();
                sendBody.append("mobile=").append(smsBillTemp.getMobile());
                sendBody.append("&spnum=").append(smsBillTemp.getSpnum());
                sendBody.append("&msg=").append(URLEncoder.encode(smsBillTemp.getMsg(),"GBK"));
                sendBody.append("&status=").append(smsBillTemp.getStatus());
                sendBody.append("&fee=").append(smsBillTemp.getFee());
                sendBody.append("&linkid=").append(smsBillTemp.getId());

                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url + "?" + sendBody.toString());

                HttpResponse response = client.execute(get);
                HttpEntity entity = response.getEntity();
                String returnBody = EntityUtils.toString(entity).trim();
                LogEnum.DEFAULT.info("同步一条话单{}，返回[{}]", smsBillTemp, returnBody);
                if ("1".equals(returnBody)) {
                    billLogService.save(billLog);
                    billTempService.deleteSmsTemp(smsBillTemp.getId());
                    return returnFunc(start);
                }
            } else {
                billLogService.save(billLog);
                billTempService.deleteSmsTemp(smsBillTemp.getId());
                return returnFunc(start);
            }
        } catch (IOException e) {
            LogEnum.DEFAULT.error("同步出现异常{}", smsBillTemp, e);
        }
        LogEnum.DEFAULT.error("同步失败，计数加1,{}", smsBillTemp);
        billTempService.addSmsSendNum(smsBillTemp.getId());

        return returnFunc(start);
    }

    private String returnFunc(long start) {
        return smsBillTemp.getMobile() + "-" + smsBillTemp.getSpnum() + smsBillTemp.getMsg() + "同步耗时：" + (System.currentTimeMillis() - start);
    }

    public SmsBillTemp getSmsBillTemp() {
        return smsBillTemp;
    }

    public void setSmsBillTemp(SmsBillTemp smsBillTemp) {
        this.smsBillTemp = smsBillTemp;
    }
}
