package com.sp.platform.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * User: yangl
 * Date: 13-5-26 下午4:32
 */
@Entity
@Table(name = "sms_bill_log")
public class SmsBillLog {
    private Integer id;
    private String mobile;
    private String spnum;
    private String msg;
    private String linkid;
    private String status;
    private Date btime;
    private Date etime;
    private String province;
    private String city;
    private int fee;
    private int sfid;
    private int cpid;
    /**
     * 扣量标志 0：不扣量 1：扣量
     */
    private int type;

    private String syncurl;
    private int parentid;

    public SmsBillLog(){}

    public SmsBillLog(String mobile, String spnum, String msg, String linkid, String status){
        this.mobile = mobile;
        this.spnum = spnum;
        this.msg = msg;
        this.linkid = linkid;
        this.status = status;
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


    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSyncurl() {
        return syncurl;
    }

    public void setSyncurl(String syncurl) {
        this.syncurl = syncurl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSpnum() {
        return spnum;
    }

    public void setSpnum(String spnum) {
        this.spnum = spnum;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SmsBillLog{" +
                "id=" + id +
                ", mobile='" + mobile + '\'' +
                ", spnum='" + spnum + '\'' +
                ", msg='" + msg + '\'' +
                ", linkid='" + linkid + '\'' +
                ", status='" + status + '\'' +
                ", btime=" + btime +
                ", etime=" + etime +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", fee=" + fee +
                ", sfid=" + sfid +
                ", cpid=" + cpid +
                ", type=" + type +
                ", syncurl='" + syncurl + '\'' +
                ", parentid=" + parentid +
                '}';
    }
}
