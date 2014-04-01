package com.sp.platform.cache;

import com.sp.platform.entity.Haoduan;
import com.sp.platform.service.HaoduanService;
import com.sp.platform.timer.AbstractBaseTimer;
import com.sp.platform.util.LogEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class HaoduanCache extends AbstractBaseTimer {
    public static final String NA = "其他";

    @Autowired
    private HaoduanService haoduanService;

    private int lastId = 0;
    private static Map<String, Haoduan> haoduanCache = new ConcurrentHashMap<String, Haoduan>();

    @Override
    public void init() {
    }

    @Override
    public void update() {
        int i = 0;
        List<Haoduan> list = haoduanService.getHaoduansGtId(lastId);
        if (list != null && list.size() > 0) {
            for (Haoduan bean : list) {
                i++;
                haoduanCache.put(bean.getCode(), bean);
                lastId = bean.getId();
            }
        }
        LogEnum.TEMP.info("缓存中共存放 " + haoduanCache.size() + " 条号段信息, 新增 " + i
                + " 条...最大ID为： " + lastId);
    }

    /**
     * 更新号段
     * @param haoduan
     */
    public static void setHaoduan(Haoduan haoduan){
        haoduanCache.put(haoduan.getCode(), haoduan);
    }

    /**
     * @param caller 手机号
     * @return 省份
     */
    public static String getProvince(String caller) {
        if (StringUtils.isEmpty(caller))
            return NA;
        String province = NA;
        Haoduan haoduan = haoduanCache.get(StringUtils.left(caller, 7));
        if (haoduan != null) {
            province = haoduan.getProvince();
            if (StringUtils.isEmpty(province))
                province = NA;
        }

        return province;
    }

    /**
     * @param caller 手机号
     * @return 地市
     */
    public static String getCity(String caller) {
        if (StringUtils.isEmpty(caller))
            return NA;
        String city = NA;
        Haoduan haoduan = haoduanCache.get(StringUtils.left(caller, 7));
        if (haoduan != null) {
            city = haoduan.getCity();
            if (StringUtils.isEmpty(city))
                city = NA;
        }

        return city;
    }
}