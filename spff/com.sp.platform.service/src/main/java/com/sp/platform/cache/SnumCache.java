package com.sp.platform.cache;

import com.sp.platform.common.Constants;
import com.sp.platform.entity.ServiceNum;
import com.sp.platform.service.ServiceNumService;
import com.sp.platform.timer.AbstractBaseTimer;
import com.sp.platform.util.LogEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: yangl
 * Date: 13-5-25 下午10:52
 */
@Service
public class SnumCache extends AbstractBaseTimer {
    /**
     * 被叫号码：通道实体类(用户日月上限)
     */
    private static Map<String, ServiceNum> cache = new ConcurrentHashMap<String, ServiceNum>();

    @Autowired
    private ServiceNumService serviceNumService;

    @Override
    public void init() {
    }

    @Override
    public void update() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Map<String, ServiceNum> temp;
        List<ServiceNum> list = serviceNumService.getAll();
        if (list != null && list.size() > 0) {
            temp = new HashMap<String, ServiceNum>();
            String[] sms;
            String key;
            for (ServiceNum bean : list) {
                if (Constants.TYPE_SMS.equals(SpInfoCache.getSpInfo(bean.getSpid()).getType())) {
                    key = bean.getCalled();
                    sms = bean.getCalled().split(Constants.split_str);
                    bean.setCalled(sms[0]);
                    bean.setSmsMsg(sms[1]);
                    temp.put(key, bean);
                } else {
                    temp.put(bean.getCalled(), bean);
                }
            }
            cache.clear();
            cache.putAll(temp);
        }
        LogEnum.TEMP.info("缓存中共存放{}条号码信息", cache.size());
    }

    /**
     * IVR
     * 根据被叫号码获取信息
     *
     * @param key
     * @return
     */
    public static ServiceNum get(String key) {
        if(key == null ){
            return null;
        }
        return cache.get(key);
    }

    public static int getSpid(String key) {
        ServiceNum serviceNum = cache.get(key);
        if (serviceNum == null) {
            return 999999999;
        }
        return serviceNum.getSpid();
    }

    public static int getDayLimit(String key) {
        ServiceNum serviceNum = cache.get(key);
        if (serviceNum == null) {
            return 0;
        }
        return serviceNum.getDaylimit();
    }

    public static int getMonthLimit(String key) {
        ServiceNum serviceNum = cache.get(key);
        if (serviceNum == null) {
            return 0;
        }
        return serviceNum.getMonthlimit();
    }

}
