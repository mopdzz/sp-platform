package com.sp.platform.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created with IntelliJ IDEA.
 * User: mopdgg
 * Date: 13-5-24
 * Time: 下午10:15
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "tbl_servicenum")
public class ServiceNum extends BaseEntity {
    private int opid;
    private int spid;
    private String spname;
    private String called;
    private int fee;
    private int daylimit;
    private int monthlimit;
    private int type;
    private String memo;
    private int status;

    private String cpname;
    private String smsMsg;

    @Transient
    public String getCpname() {
        return cpname;
    }

    public void setCpname(String cpname) {
        this.cpname = cpname;
    }

    @Transient
    public String getSmsMsg() {
        return smsMsg;
    }

    public void setSmsMsg(String smsMsg) {
        this.smsMsg = smsMsg;
    }

    @Transient
    public String getSpname() {
        return spname;
    }

    public void setSpname(String spname) {
        this.spname = spname;
    }

    public int getOpid() {
        return opid;
    }

    public void setOpid(int opid) {
        this.opid = opid;
    }

    public int getSpid() {
        return spid;
    }

    public void setSpid(int spid) {
        this.spid = spid;
    }

    public String getCalled() {
        return called;
    }

    public void setCalled(String called) {
        this.called = called;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getDaylimit() {
        return daylimit;
    }

    public void setDaylimit(int daylimit) {
        this.daylimit = daylimit;
    }

    public int getMonthlimit() {
        return monthlimit;
    }

    public void setMonthlimit(int monthlimit) {
        this.monthlimit = monthlimit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ServiceNum{" +
                "opid=" + opid +
                ", spid=" + spid +
                ", spname='" + spname + '\'' +
                ", called='" + called + '\'' +
                ", fee=" + fee +
                ", daylimit=" + daylimit +
                ", monthlimit=" + monthlimit +
                ", type=" + type +
                ", memo='" + memo + '\'' +
                ", status=" + status +
                '}';
    }
}
