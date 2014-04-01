package com.sp.platform.cache;

import com.sp.platform.entity.ProvReduce;
import com.sp.platform.service.ProvReduceService;
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
 * Date: 13-5-26 下午4:21
 */
@Service
public class ProvReduceCache extends AbstractBaseTimer{

    /**
     * 被叫号码_省份：号码省份配置类（上限、扣量）
     */
    private static Map<String, ProvReduce> cache = new ConcurrentHashMap<String, ProvReduce>();

    @Autowired
    private ProvReduceService provReduceService;

    @Override
    public void init() {
    }

    @Override
    public void update() {
        Map<String, ProvReduce> temp;
        List<ProvReduce> list = provReduceService.getAll();
        if(list != null && list.size() > 0){
            temp = new HashMap<String, ProvReduce>();
            for(ProvReduce bean : list){
                temp.put(bean.getCalled() + "_" + bean.getProvince(), bean);
            }
            cache.clear();
            cache.putAll(temp);
        }
        LogEnum.TEMP.info("缓存中共存放{}条省份扣量信息", cache.size());
    }

    public static ProvReduce get(String key){
        return cache.get(key);
    }

    public static int getProvReduce(String key){
        ProvReduce provReduce = cache.get(key);
        if(provReduce != null){
            return provReduce.getReduce();
        }
        return 0;
    }

    public static int getDayLimit(String key){
        ProvReduce provReduce = cache.get(key);
        if(provReduce != null){
            return provReduce.getDaylimit();
        }
        return 0;
    }

    public static int getMonthLimit(String key){
        ProvReduce provReduce = cache.get(key);
        if(provReduce != null){
            return provReduce.getMonthlimit();
        }
        return 0;
    }
}
