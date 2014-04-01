package com.sp.platform.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-2-13
 * Time: 下午9:50
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "tbl_month")
public class MonthData extends BaseEntity {
    private int spid;
    private String mobileno;
    private String requesttime;
    private String proviecode;
    private String ringno;
    private String subchannelid;
    private String ordertype;
    private String status;
    private int type;

    public int getSpid() {
        return spid;
    }

    public void setSpid(int spid) {
        this.spid = spid;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getRequesttime() {
        return requesttime;
    }

    public void setRequesttime(String requesttime) {
        this.requesttime = requesttime;
    }

    public String getProviecode() {
        return proviecode;
    }

    public void setProviecode(String proviecode) {
        this.proviecode = proviecode;
    }

    public String getRingno() {
        return ringno;
    }

    public void setRingno(String ringno) {
        this.ringno = ringno;
    }

    public String getSubchannelid() {
        return subchannelid;
    }

    public void setSubchannelid(String subchannelid) {
        this.subchannelid = subchannelid;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MonthData{" +
                "spid=" + spid +
                ", mobileno='" + mobileno + '\'' +
                ", requesttime='" + requesttime + '\'' +
                ", proviecode='" + proviecode + '\'' +
                ", ringno='" + ringno + '\'' +
                ", subchannelid='" + subchannelid + '\'' +
                ", ordertype='" + ordertype + '\'' +
                ", status='" + status + '\'' +
                ", type=" + type +
                '}';
    }
}
