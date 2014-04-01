package com.sp.platform.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;

@Repository
public class CacheCheckUser {
    @Autowired
    protected PropertyUtils propertyUtils;

    /**
     * 存放缓存数据的文件路径
     */
    protected static String fileRootPath;
    protected static final SimpleDateFormat sdfMonth = new SimpleDateFormat(
            "yyyy-MM");
    protected static SimpleDateFormat sdfDay = new SimpleDateFormat(
            "yyyy-MM-dd");

    /**
     * 获得缓存文件的路径
     */
    public String getLimitPath(CacheFilePath cacheFilePath) {
        if (StringUtils.isEmpty(fileRootPath)) {
            fileRootPath = propertyUtils.getProperty("limit.fileRootPath");
        }
        StringBuilder stringBuilder = new StringBuilder(fileRootPath);
        String timeString;
        switch (cacheFilePath) {
            case CALLED_DAY_COUNT:
                timeString = sdfDay.format(new Date());
                stringBuilder.append("day/").append(timeString).append("/")
                        .append(timeString).append("_called_count.txt");
                break;
            case CALLED_DAY_FEE:
                timeString = sdfDay.format(new Date());
                stringBuilder.append("day/").append(timeString).append("/")
                        .append(timeString).append("_called_fee.txt");
                break;
            case CALLED_MONTH_FEE:
                timeString = sdfMonth.format(new Date());
                stringBuilder.append("month/").append(timeString).append("/")
                        .append(timeString).append("_called_fee.txt");
                break;
            case CALLED_PROVINCE_DAY_FEE:
                timeString = sdfDay.format(new Date());
                stringBuilder.append("day/").append(timeString).append("/")
                        .append(timeString).append("_province_called_fee.txt");
                break;
            case CALLED_PROVINCE_MONTH_FEE:
                timeString = sdfMonth.format(new Date());
                stringBuilder.append("month/").append(timeString).append("/")
                        .append(timeString).append("_province_called_fee.txt");
                break;
            case CALLED_PROVINCE_DAY_COUNT:
                timeString = sdfDay.format(new Date());
                stringBuilder.append("day/").append(timeString).append("/")
                        .append(timeString).append("_province_called_count.txt");
                break;
            case CALLER_DAY_FEE :
                timeString = sdfDay.format(new Date());
                stringBuilder.append("day/").append(timeString).append("/")
                        .append(timeString).append("_caller_fee.txt");
                break;
            case CALLER_MONTH_FEE :
                timeString = sdfMonth.format(new Date());
                stringBuilder.append("month/").append(timeString).append("/")
                        .append(timeString).append("_caller_fee.txt");
                break;
            default:
                return "";
        }
        return stringBuilder.toString();
    }

    /**
     * 长号码每天拨打次数
     */
    public int getCalledDayCount(String key) {
        String filePath = getLimitPath(CacheFilePath.CALLED_DAY_COUNT);
        FileMemoryCache rfm = FileMemoryCache.getInstance(
                CacheFilePath.CALLED_DAY_COUNT.getName(), filePath, sdfDay);
        return rfm.getNodeCount(key);
    }

    /**
     * 长号码每天拨打费用
     */
    public int getCalledDayFee(String key) {
        String filePath = getLimitPath(CacheFilePath.CALLED_DAY_FEE);
        FileMemoryCache rfm = FileMemoryCache.getInstance(
                CacheFilePath.CALLED_DAY_FEE.getName(), filePath, sdfDay);
        return rfm.getNodeCount(key) / 100;
    }

    /**
     * 长号码每月拨打费用
     */
    public int getCalledMonthFee(String key) {
        String filePath = getLimitPath(CacheFilePath.CALLED_MONTH_FEE);
        FileMemoryCache rfm = FileMemoryCache.getInstance(
                CacheFilePath.CALLED_MONTH_FEE.getName(), filePath, sdfMonth);
        return rfm.getNodeCount(key) / 100;
    }

    /**
     * 话单接口中，增加通道缓存费用
     *
     * @param key
     * @param fee
     */
    public void addCalledFee(String key, int fee, boolean flag) {
        String filePath = getLimitPath(CacheFilePath.CALLED_DAY_COUNT);
        FileMemoryCache rfm = FileMemoryCache.getInstance(CacheFilePath.CALLED_DAY_COUNT.getName(),
                filePath, sdfDay);
        rfm.getAndAdd(key);

        if(!flag){
            filePath = getLimitPath(CacheFilePath.CALLED_DAY_FEE);
            rfm = FileMemoryCache.getInstance(
                    CacheFilePath.CALLED_DAY_FEE.getName(), filePath, sdfDay);
            rfm.getAndAdd(key, fee);

            filePath = getLimitPath(CacheFilePath.CALLED_MONTH_FEE);
            rfm = FileMemoryCache.getInstance(CacheFilePath.CALLED_MONTH_FEE.getName(),
                    filePath, sdfMonth);
            rfm.getAndAdd(key, fee);
        }
    }

    /**
     * 话单接口中，增加通道缓存费用
     *
     * @param key
     * @param fee
     */
    public void addCalledProvinceFee(String key, int fee, boolean flag) {
        String filePath = getLimitPath(CacheFilePath.CALLED_PROVINCE_DAY_COUNT);
        FileMemoryCache rfm = FileMemoryCache.getInstance(CacheFilePath.CALLED_PROVINCE_DAY_COUNT.getName(),
                filePath, sdfDay);
        rfm.getAndAdd(key);

        if(!flag){
            filePath = getLimitPath(CacheFilePath.CALLED_PROVINCE_DAY_FEE);
            rfm = FileMemoryCache.getInstance(
                    CacheFilePath.CALLED_PROVINCE_DAY_FEE.getName(), filePath, sdfDay);
            rfm.getAndAdd(key, fee);

            filePath = getLimitPath(CacheFilePath.CALLED_PROVINCE_MONTH_FEE);
            rfm = FileMemoryCache.getInstance(
                    CacheFilePath.CALLED_PROVINCE_MONTH_FEE.getName(), filePath, sdfMonth);
            rfm.getAndAdd(key, fee);
        }
    }

    /**
     * 长号码每天拨打次数
     */
    public int getCalledProvinceDayCount(String key) {
        String filePath = getLimitPath(CacheFilePath.CALLED_PROVINCE_DAY_COUNT);
        FileMemoryCache rfm = FileMemoryCache.getInstance(
                CacheFilePath.CALLED_PROVINCE_DAY_COUNT.getName(), filePath, sdfDay);
        return rfm.getNodeCount(key);
    }

    /**
     * 长号码每天拨打费用
     */
    public int getCalledProvinceDayFee(String key) {
        String filePath = getLimitPath(CacheFilePath.CALLED_PROVINCE_DAY_FEE);
        FileMemoryCache rfm = FileMemoryCache.getInstance(
                CacheFilePath.CALLED_PROVINCE_DAY_FEE.getName(), filePath, sdfDay);
        return rfm.getNodeCount(key) / 100;
    }

    /**
     * 长号码每月拨打费用
     */
    public int getCalledProvinceMonthFee(String key) {
        String filePath = getLimitPath(CacheFilePath.CALLED_PROVINCE_MONTH_FEE);
        FileMemoryCache rfm = FileMemoryCache.getInstance(
                CacheFilePath.CALLED_PROVINCE_MONTH_FEE.getName(), filePath, sdfMonth);
        return rfm.getNodeCount(key) / 100;
    }

    /**
     * 话单接口，增加用户缓存费用
     *
     * @param key
     * @param fee
     */
    public void addCallerFee(String key, int fee) {
        String filePath = getLimitPath(CacheFilePath.CALLER_DAY_FEE);
        FileMemoryCache rfm = FileMemoryCache.getInstance(
                CacheFilePath.CALLER_DAY_FEE.getName(), filePath, sdfDay);
        rfm.getAndAdd(key, fee);

        filePath = getLimitPath(CacheFilePath.CALLER_MONTH_FEE);
        rfm = FileMemoryCache.getInstance(
                CacheFilePath.CALLER_MONTH_FEE.getName(), filePath, sdfMonth);
        rfm.getAndAdd(key, fee);
    }

    /**
     * 用户每天拨打费用
     */
    public int getCallerDayFee(String key) {
        String filePath = getLimitPath(CacheFilePath.CALLER_DAY_FEE);
        FileMemoryCache rfm = FileMemoryCache.getInstance(
                CacheFilePath.CALLER_DAY_FEE.getName(), filePath, sdfDay);
        return rfm.getNodeCount(key) / 100;
    }

    /**
     * 用户每月拨打费用
     */
    public int getCallerMonthFee(String key) {
        String filePath = getLimitPath(CacheFilePath.CALLER_MONTH_FEE);
        FileMemoryCache rfm = FileMemoryCache.getInstance(
                CacheFilePath.CALLER_MONTH_FEE.getName(), filePath, sdfMonth);
        return rfm.getNodeCount(key) / 100;
    }
}
