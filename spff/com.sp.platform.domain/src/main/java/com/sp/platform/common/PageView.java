package com.sp.platform.common;

import java.util.Date;

/**
 * User: yangl
 * Date: 13-5-24 下午10:48
 */
public class PageView {
    private int id;
    private int opid;
    private int spid;
    private int cpid;
    private String caller;
    private String called;
    private String btime;
    private String etime;
    private int fee = 100;
    private int type = 1;
    private int status = 1;
    private String memo;
    private int daylimit;
    private int monthlimit;
    private int deductRate;

    private int parentId;
    private int role;
    private String name;
    private String passwd;
    private String showname;
    private String syncurl;
    private Date ctime;

    private String date;
    private String message;
    private String province;
    private String group;
    private String group2;
    private int cpflag;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCpflag() {
        return cpflag;
    }

    public void setCpflag(int cpflag) {
        this.cpflag = cpflag;
    }

    public String getGroup2() {
        return group2;
    }

    public void setGroup2(String group2) {
        this.group2 = group2;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }


    public int getMonthlimit() {
        return monthlimit;
    }

    public void setMonthlimit(int monthlimit) {
        this.monthlimit = monthlimit;
    }

    public int getDeductRate() {
        return deductRate;
    }

    public void setDeductRate(int deductRate) {
        this.deductRate = deductRate;
    }

    public int getDaylimit() {
        return daylimit;
    }

    public void setDaylimit(int daylimit) {
        this.daylimit = daylimit;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCpid() {
        return cpid;
    }

    public void setCpid(int cpid) {
        this.cpid = cpid;
    }

    public String getBtime() {
        return btime;
    }

    public void setBtime(String btime) {
        this.btime = btime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getShowname() {
        return showname;
    }

    public void setShowname(String showname) {
        this.showname = showname;
    }

    public String getSyncurl() {
        return syncurl;
    }

    public void setSyncurl(String syncurl) {
        this.syncurl = syncurl;
    }

    @Override
    public String toString() {
        return "PageView{" +
                "id=" + id +
                ", opid=" + opid +
                ", spid=" + spid +
                ", cpid=" + cpid +
                ", caller='" + caller + '\'' +
                ", called='" + called + '\'' +
                ", btime='" + btime + '\'' +
                ", etime='" + etime + '\'' +
                ", fee=" + fee +
                ", type=" + type +
                ", status=" + status +
                ", memo='" + memo + '\'' +
                ", daylimit=" + daylimit +
                ", monthlimit=" + monthlimit +
                ", deductRate=" + deductRate +
                ", parentId=" + parentId +
                ", role=" + role +
                ", name='" + name + '\'' +
                ", passwd='" + passwd + '\'' +
                ", showname='" + showname + '\'' +
                ", syncurl='" + syncurl + '\'' +
                ", ctime=" + ctime +
                ", message='" + message + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}
