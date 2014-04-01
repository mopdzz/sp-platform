package com.sp.platform.common;

/**
 * User: yangl
 * Date: 13-6-2 下午3:33
 */
public enum GroupType {
    /**
     * 日期
     */
    GROUP_TIME(1),
    /**
     * 长号
     */
    GROUP_CALLED(2),
    /**
     * 通道
     */
    GROUP_SP(3),
    /**
     * 主渠道
     */
    GROUP_CP_PARENT(4),
    /**
     * 子渠道
     */
    GROUP_CP(5),
    /**
     * 省份
     */
    GROUP_PROVINCE(6),
    /**
     * 地市
     */
    GROUP_CITY(7),
    /**
     * 小时
     */
    GROUP_HOUR(8);

    private int type;

    private GroupType(int type) {
        this.type = type;
    }

    public boolean isEquals(int type){
        return this.type == type;
    }

    public int getType(){
        return this.type;
    }
}
