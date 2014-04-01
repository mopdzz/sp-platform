package com.sp.platform.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * User: yangl
 * Date: 13-5-29 下午10:56
 */
public class ProvinceVo {
    private String name;
    private String province;

    public ProvinceVo(){}
    public ProvinceVo(String name, String province){
        this.name = name;
        this.province = province;
    }

    public static List<ProvinceVo> list = new ArrayList<ProvinceVo>();

    private final static String[] p = new String[] { "重庆", "湖南", "贵州", "湖北",
            "云南", "西藏", "四川", "陕西", "甘肃", "宁夏", "青海", "河南", "新疆", "广东", "山西",
            "吉林", "北京", "江苏", "上海", "天津", "河北", "辽宁", "黑龙江", "内蒙古", "福建", "山东",
            "安徽", "浙江", "江西", "海南", "广西" };
    static{
        for(String key : p){
            list.add(new ProvinceVo(key, key));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
