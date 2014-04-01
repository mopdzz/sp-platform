package com.sp.platform.util;

public enum CacheFilePath {

    CALLED_DAY_COUNT("CALLED_DAY_COUNT"), CALLED_DAY_FEE("CALLED_DAY_FEE"), CALLED_MONTH_FEE("CALLED_MONTH_FEE"),
    CALLED_PROVINCE_DAY_FEE("CALLED_PROVINCE_DAY_FEE"), CALLED_PROVINCE_MONTH_FEE("CALLED_PROVINCE_MONTH_FEE"), CALLED_PROVINCE_DAY_COUNT("CALLED_PROVINCE_DAY_COUNT"),
    CALLER_DAY_FEE("CALLER_DAY_FEE"), CALLER_MONTH_FEE("CALLER_MONTH_FEE");

    private final String name;

    CacheFilePath(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
