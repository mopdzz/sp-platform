package com.sp.platform.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: yangl
 * Date: 13-5-26 下午4:06
 */
@Entity
@Table(name = "tbl_province_reduce")
public class ProvReduce  extends BaseEntity{
    private String called;
    private String province;
    private Integer reduce;
    private int daylimit;
    private int monthlimit;

    public int getMonthlimit() {
        return monthlimit;
    }

    public void setMonthlimit(int monthlimit) {
        this.monthlimit = monthlimit;
    }

    public int getDaylimit() {
        return daylimit;
    }

    public void setDaylimit(int daylimit) {
        this.daylimit = daylimit;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getReduce() {
        return reduce;
    }

    public void setReduce(Integer reduce) {
        this.reduce = reduce;
    }

    public String getCalled() {
        return called;
    }

    public void setCalled(String called) {
        this.called = called;
    }
}
