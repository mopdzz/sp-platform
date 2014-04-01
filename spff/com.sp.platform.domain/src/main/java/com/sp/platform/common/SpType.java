package com.sp.platform.common;

/**
 * User: yangl
 * Date: 13-6-2 下午3:33
 */
public enum SpType {
    IVR("IVR"), SMS("SMS");

    private String type;

    private SpType(String type) {
        this.type = type;
    }

    public boolean isEquals(String type) {
        return this.type.equals(type);
    }

    public String getType() {
        return this.type;
    }
}
