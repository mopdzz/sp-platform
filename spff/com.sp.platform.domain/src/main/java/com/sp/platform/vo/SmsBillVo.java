package com.sp.platform.vo;

/**
 * User: yangl
 * Date: 13-5-30 上午3:03
 */
public class SmsBillVo {
    private String counts;
    private String salecounts;
    private String users;
    private String fees;
    private String salefee;
    private String limitfee;
    private String limitrate;
    private String times;
    private String group;
    private String arpu;
    private String href;
    private int type;

    public String getSalefee() {
        return salefee;
    }

    public void setSalefee(String salefee) {
        this.salefee = salefee;
    }

    public String getSalecounts() {
        return salecounts;
    }

    public void setSalecounts(String salecounts) {
        this.salecounts = salecounts;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getLimitfee() {
        return limitfee;
    }

    public void setLimitfee(String limitfee) {
        this.limitfee = limitfee;
    }

    public String getLimitrate() {
        return limitrate;
    }

    public void setLimitrate(String limitrate) {
        this.limitrate = limitrate;
    }

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getArpu() {
        return arpu;
    }

    public void setArpu(String arpu) {
        this.arpu = arpu;
    }

    @Override
    public String toString() {
        return "BillVo{" +
                "counts='" + counts + '\'' +
                ", users='" + users + '\'' +
                ", fees='" + fees + '\'' +
                ", times='" + times + '\'' +
                ", group='" + group + '\'' +
                ", arpu='" + arpu + '\'' +
                '}';
    }
}
