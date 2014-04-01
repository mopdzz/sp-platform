package com.sp.platform.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * User: yangl
 * Date: 13-5-26 下午4:32
 */
@Entity
@Table(name = "tbl_bill_log")
public class BillLog {
    private Integer id;
    private String caller;
    private String called;
    private Date btime;
    private Date etime;
    private String province;
    private String city;
    private int time;
    private int fee;
    private int sfid;
    private int cpid;
    /**
     * 扣量标志 0：不扣量 1：扣量
     */
    private int type;

    private String syncurl;
    private int parentid;

    public BillLog(){}
    public BillLog(String caller, String called, Date btime, Date etime){
        this.caller = caller;
        this.called = called;
        this.btime = btime;
        this.etime = etime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public String getSyncurl() {
        return syncurl;
    }

    public void setSyncurl(String syncurl) {
        this.syncurl = syncurl;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCalled() {
        return called;
    }

    public void setCalled(String called) {
        this.called = called;
    }

    public Date getBtime() {
        return btime;
    }

    public void setBtime(Date btime) {
        this.btime = btime;
    }

    public Date getEtime() {
        return etime;
    }

    public void setEtime(Date etime) {
        this.etime = etime;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getSfid() {
        return sfid;
    }

    public void setSfid(int sfid) {
        this.sfid = sfid;
    }

    public int getCpid() {
        return cpid;
    }

    public void setCpid(int cpid) {
        this.cpid = cpid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "BillLog{" +
                "id=" + id +
                ", caller='" + caller + '\'' +
                ", called='" + called + '\'' +
                ", btime=" + btime +
                ", etime=" + etime +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", time=" + time +
                ", fee=" + fee +
                ", sfid=" + sfid +
                ", cpid=" + cpid +
                ", type=" + type +
                '}';
    }
}
