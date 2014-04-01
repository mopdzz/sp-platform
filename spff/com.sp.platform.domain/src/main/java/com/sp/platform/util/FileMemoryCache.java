package com.sp.platform.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileMemoryCache {
    private static Logger logger = LoggerFactory.getLogger(FileMemoryCache.class);

    public static Map<String, FileMemoryCache> _cache = new ConcurrentHashMap<String, FileMemoryCache>();

    public String objName; // 对象所做操作的名称
    public String filePath; // 存放文件的路径
    public SimpleDateFormat sdf = null;
    private long lastSaveTime = 0; // 上次保存的时间
    private long lastUpdateTime = 0; // 上次修改的时间
    private long refreshInterval = 60000L; // 文件刷时间
    private boolean isLoad = false; // 文件是否已经载入
    private boolean isSaving = false; // 文件是否正在保存
    private Map<String, CacheNode> table = new ConcurrentHashMap<String, CacheNode>(); // 存放相应的上限信息

    private final ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(1);

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            saveFile();
        }
    }

    public FileMemoryCache() {
    }

    public FileMemoryCache(String objName) {
        this.objName = objName;
    }

    public FileMemoryCache(String objName, String filePath, SimpleDateFormat sdf) {
        this.objName = objName;
        this.filePath = filePath;
        this.sdf = sdf;
        scheduler.scheduleAtFixedRate(new RefreshTask(), refreshInterval,
                refreshInterval, TimeUnit.MILLISECONDS);
    }

    public static FileMemoryCache getInstance(String objName, String filePath,
                                              SimpleDateFormat sdf) {
        FileMemoryCache instance = _cache.get(objName);
        if (instance == null) {
            synchronized (FileMemoryCache.class) {
                if (_cache.get(objName) == null) {
                    instance = new FileMemoryCache(objName, filePath, sdf);
                    _cache.put(objName, instance);
                    instance.lastSaveTime = System.currentTimeMillis();
                }
            }
        } else {
            if (!filePath.equals(instance.filePath)) {
                _cache.clear();
                instance.filePath = filePath;
                instance.sdf = sdf;
            }
        }
        return instance;
    }

    /**
     * 得到缓存中的数据
     */
    public int getNodeCount(String key) {
        if (StringUtils.isEmpty(key)) {
            return 0;
        }
        if (!isLoad) {
            loadFile();
        }
        CacheNode node = table.get(key);
        if (node == null) {
            return 0;
        } else {
            return node.getCount();
        }
    }

    /**
     * 获得缓存数据并加1
     */
    public int getAndAdd(String key) {
        return getAndAdd(key, 1);
    }

    /**
     * 获得缓存数据并加 count
     */
    public int getAndAdd(String key, int count) {
        if (StringUtils.isEmpty(key)) {
            return 0;
        }
        if (!isLoad) {
            loadFile();
        }
        String now_date = sdf.format(new java.util.Date());
        CacheNode node = table.get(key);
        lastUpdateTime = System.currentTimeMillis();
        if (node == null) {
            CacheNode newNode = new CacheNode(key, now_date);
            newNode.setCount(count);
            table.put(key, newNode);
            return count;
        } else {
            if (now_date.equals(node.getNodeDate())) {
                return node.addCount(count);
            } else {
                node.setNodeDate(now_date);
                node.setCount(count);
                return count;
            }
        }
    }

    protected void loadFile() {
        File file = new File(filePath);
        lastSaveTime = System.currentTimeMillis();
        if (!file.exists()) { // 文件不存在
            isLoad = true;
            return;
        }
        table.clear(); // 清空内存
        try {
            String datestr = sdf.format(new Date());
            LineNumberReader in = new LineNumberReader(new BufferedReader(
                    new FileReader(file)));
            String line = in.readLine();
            while (StringUtils.isNotEmpty(line)) {
                String[] as = StringUtils.split(line, ":");
                try {
                    if (as.length > 1) {
                        CacheNode node = new CacheNode(as[0], datestr);
                        node.setNodeDate(datestr);
                        node.setCount(getIntValue(as[1], 0));
                        table.put(node.getNodeKey(), node);
                    }
                } catch (Exception eex) {
                    logger.error("objectName=" + this.objName
                            + ", loadFile is error:" + eex);
                }
                line = in.readLine();
            }
        } catch (Exception e) {
            logger.error("objectName=" + this.objName + ", loadFile is error:"
                    + e);
        }
        isLoad = true;
        lastSaveTime = System.currentTimeMillis();
    }

    /**
     * 转化数字
     */
    public int getIntValue(String v, int def) {
        if (v == null)
            return def;
        try {
            return Integer.parseInt(v.trim());
        } catch (Exception ex) {
            return def;
        }
    }

    protected void saveFile() {
        if (!isLoad || isSaving) {
            return;
        }
        if (table.size() == 0) {
            return;
        }
        if (lastUpdateTime < lastSaveTime) {
            return;
        }
        isSaving = true;
        String today = sdf.format(new java.util.Date());
        try {
            String aname = "";
            CacheNode node = null;
            List<String> body = new LinkedList<String>();
            for (Object okey : table.keySet()) {
                aname = (String) okey;
                node = (CacheNode) table.get(aname);
                if (node.getNodeDate().equals(today)) {
                    if (node.getCount() > 0) {
                        body.add(node.getNodeKey() + ":" + node.getCount());
                    }
                }
            }
            if (body.size() > 0)
                FileUtils.writeLines(new File(filePath), body);
            lastSaveTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
            LogEnum.DEFAULT.error("write tag error[" + filePath + "]:" + e.getMessage());
        } finally {
            isSaving = false;
        }
        lastSaveTime = System.currentTimeMillis();
    }
}
