package com.sp.platform.cache;

import com.sp.platform.common.Constants;
import com.sp.platform.entity.SpInfo;
import com.sp.platform.service.SpInfoService;
import com.sp.platform.timer.AbstractBaseTimer;
import com.sp.platform.util.LogEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: yangl
 * Date: 13-5-24 下午11:52
 */
@Service
public class SpInfoCache extends AbstractBaseTimer {
    private static Map<Integer, SpInfo> cache = new ConcurrentHashMap<Integer, SpInfo>();
    private static Map<String, Map<String, String>> spIvrConfig = new ConcurrentHashMap<String, Map<String, String>>();
    private static Map<String, Map<String, String>> spSmsConfig = new ConcurrentHashMap<String, Map<String, String>>();

    @Autowired
    private SpInfoService spInfoService;

    @Override
    public void init() {
    }

    @Override
    public void update() {
        Map<Integer, SpInfo> temp;
        Map<String, Map<String, String>> temp2;
        Map<String, Map<String, String>> temp3;
        List<SpInfo> list = spInfoService.getAll();
        if (list != null && list.size() > 0) {
            temp = new HashMap<Integer, SpInfo>();
            temp2 = new HashMap<String, Map<String, String>>();
            temp3 = new HashMap<String, Map<String, String>>();

            for (SpInfo spInfo : list) {
                temp.put(spInfo.getId(), spInfo);
                if (Constants.TYPE_SMS.equals(spInfo.getType())) {
                    temp3.put(String.valueOf(spInfo.getId()), convertSmsSpInfo(spInfo));
                } else {
                    temp2.put(String.valueOf(spInfo.getId()), convertIvrSpInfo(spInfo));
                }
            }
            cache.clear();
            cache.putAll(temp);
            spIvrConfig.clear();
            spIvrConfig.putAll(temp2);
            spSmsConfig.clear();
            spSmsConfig.putAll(temp3);
        }
        LogEnum.TEMP.info("缓存中共存放{}条通道信息, {}条IVR, {}条SMS", cache.size(), spIvrConfig.size(), spSmsConfig.size());
    }

    private Map<String, String> convertIvrSpInfo(SpInfo spInfo) {
        Map<String, String> temp2 = new HashMap<String, String>();
        Map<String, String> temp3 = new HashMap<String, String>();
        String[] s1 = spInfo.getSyncurl().split("&");
        String[] s2;
        for (String str : s1) {
            s2 = str.split("=");
            temp2.put(s2[0], s2[1]);
        }

        temp3.put("caller", temp2.get("caller"));
        temp3.put("called", temp2.get("called"));
        temp3.put("btime", temp2.get("btime"));
        temp3.put("etime", temp2.get("etime"));
        temp3.put("format", temp2.get("format"));
        temp3.put("fee", temp2.get("fee"));
        temp3.put("time", temp2.get("time"));
        temp3.put("return", temp2.get("return"));

        return temp3;
    }

    private Map<String, String> convertSmsSpInfo(SpInfo spInfo) {
        Map<String, String> temp2 = new HashMap<String, String>();
        Map<String, String> temp3 = new HashMap<String, String>();
        String[] s1 = spInfo.getSyncurl().split("&");
        String[] s2;
        for (String str : s1) {
            s2 = str.split("=");
            temp2.put(s2[0], s2[1]);
        }

        temp3.put("mobile", temp2.get("mobile")); //手机号
        temp3.put("spnum", temp2.get("spnum"));//长号
        temp3.put("msg", temp2.get("msg"));//指令
        temp3.put("linkid", temp2.get("linkid"));//Linkid
        temp3.put("status", temp2.get("status"));//状态报告 DELIVRD为成功
        temp3.put("return", temp2.get("return"));
        temp3.put("DELIVRD", temp2.get("DELIVRD"));

        return temp3;
    }

    /**
     * 获取通道信息
     *
     * @param key 通道ID
     * @return
     */
    public static SpInfo getSpInfo(int key) {
        return cache.get(key);
    }

    /**
     * 只获取通道名称
     *
     * @param key 通道ID
     * @return
     */
    public static String getSpInfoName(int key) {
        if (cache.containsKey(key)) {
            return cache.get(key).getName();
        } else {
            return "未知";
        }
    }

    /**
     * 获取所有通道列表(有些场景读取缓存中)
     *
     * @return
     */
    public static List<SpInfo> getSpInfos() {
        List<SpInfo> temp = new ArrayList<SpInfo>();
        for (Integer key : cache.keySet()) {
            temp.add(cache.get(key));
        }
        return temp;
    }

    /**
     * IVR 通道同步信息
     *
     * @param key 通道ID
     * @return
     */
    public static Map<String, String> getIvrSyncInfo(String key) {
        return spIvrConfig.get(key);
    }

    /**
     * SMS 通道同步信息
     *
     * @param key 通道ID
     * @return
     */
    public static Map<String, String> getSmsSyncInfo(String key) {
        return spSmsConfig.get(key);
    }
}
