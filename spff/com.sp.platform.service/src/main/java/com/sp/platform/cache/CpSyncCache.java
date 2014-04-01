package com.sp.platform.cache;

import com.sp.platform.entity.CpNum;
import com.sp.platform.entity.User;
import com.sp.platform.service.CpNumService;
import com.sp.platform.service.UserService;
import com.sp.platform.timer.AbstractBaseTimer;
import com.sp.platform.util.LogEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: yangl
 * Date: 13-5-30 上午12:41
 */
@Service
public class CpSyncCache extends AbstractBaseTimer {

    /**
     * 被叫号码：同步地址
     */
    private static Map<String, String> called_syncurl_cache = new ConcurrentHashMap<String, String>();
    /**
     * 被叫号码：渠道ID
     */
    private static Map<String, CpNum> called_cp_cache = new ConcurrentHashMap<String, CpNum>();
    /**
     * 渠道
     */
    private static Map<Integer, User> cp_cache = new ConcurrentHashMap<Integer, User>();

    @Autowired
    private UserService userService;
    @Autowired
    private CpNumService cpNumService;

    @Override
    public void init() {
    }

    @Override
    public void update() {
        //临时Map， 渠道ID：同步地址
        Map<Integer, String> temp = new HashMap<Integer, String>();
        //被叫号码：同步地址
        Map<String, String> temp2 = new HashMap<String, String>();
        //存放渠道信息
        Map<Integer, User> temp3 = new HashMap<Integer, User>();
        Map<String, CpNum> temp4 = new HashMap<String, CpNum>();

        //主帐号
        List<User> list = userService.getByRole(10);
        if (list != null) {
            for (User user : list) {
                temp3.put(user.getId(), user);

                if (StringUtils.isNotBlank(user.getSyncurl())) {
                    temp.put(user.getId(), user.getSyncurl());
                }
            }
        }
        //子帐号
        String url;
        list = userService.getByRole(11);
        if (list != null) {
            for (User user : list) {
                temp3.put(user.getId(), user);

                if (StringUtils.isNotBlank(user.getSyncurl())) {
                    temp.put(user.getId(), user.getSyncurl());
                } else {
                    url = temp.get(user.getParentId());
                    if (StringUtils.isNotBlank(url)) {
                        temp.put(user.getId(), url);
                    }
                }
            }
        }
        cp_cache.clear();
        cp_cache.putAll(temp3);

        String syncUrl;
        List<CpNum> numList = cpNumService.getAll();
        for (CpNum cpNum : numList) {
            cpNum.setCpname(getCpName(cpNum.getCpid()));
            temp4.put(cpNum.getCalled(), cpNum);
            syncUrl = temp.get(cpNum.getCpid());
            if (syncUrl != null) {
                temp2.put(cpNum.getCalled(), syncUrl);
            }
        }
        called_syncurl_cache.clear();
        called_syncurl_cache.putAll(temp2);
        called_cp_cache.clear();
        called_cp_cache.putAll(temp4);
        LogEnum.TEMP.info("缓存中共存放{}条渠道信息, {}条号码同步信息, {}条号码与渠道对应信息",
                cp_cache.size(), called_syncurl_cache.size(), called_cp_cache.size());
    }

    /**
     * 根据被叫查同步地址
     *
     * @param called
     * @return
     */
    public static String getSyncUrl(String called) {
        return called_syncurl_cache.get(called);
    }

    /**
     * 根据被叫查渠道
     *
     * @param called
     * @return
     */
    public static CpNum getCp(String called) {
        if(StringUtils.isBlank(called)){
            return null;
        }
        return called_cp_cache.get(called);
    }

    /**
     * 根据被叫查渠道ID
     *
     * @param called
     * @return
     */
    public static int getCpId(String called) {
        CpNum cpNum = getCp(called);
        if (cpNum == null) {
            return 0;
        }
        return cpNum.getCpid();
    }

    /**
     * 根据渠道ID查渠道名称
     *
     * @param key 渠道ID
     * @return
     */
    public static String getCpName(Integer key) {
        User user = cp_cache.get(key);
        if (user == null) {
            return "未知渠道";
        }
        return user.getShowname();
    }

    /**
     * 查渠道父ID
     *
     * @param key
     * @return
     */
    public static int getParentId(Integer key) {
        User user = cp_cache.get(key);
        if (user == null) {
            return 0;
        }
        return user.getParentId();
    }

    /**
     * 获取所有渠道列表(有些场景读取缓存中)
     *
     * @return
     */
    public static List<User> getCpInfos() {
        List<User> temp = new ArrayList<User>();
        for (Integer key : cp_cache.keySet()) {
            temp.add(cp_cache.get(key));
        }
        return temp;
    }

    public static int getDayLimit(String called) {
        CpNum cpNum = getCp(called);
        if (cpNum != null) {
            return cpNum.getDaylimit();
        }
        return 0;
    }

    public static int getMonthLimit(String called) {
        CpNum cpNum = getCp(called);
        if (cpNum != null) {
            return cpNum.getMonthlimit();
        }
        return 0;
    }
}
