package com.sp.platform.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * User: yangl
 * Date: 13-5-29 上午12:27
 */
@Entity
@Table(name = "tbl_cp_called")
public class CpNum extends BaseEntity {
    private int cpid;
    private String called;
    private String cpname;
    private int daylimit;
    private int monthlimit;
    private int reduce;
    private String blackinfo;

    public int getReduce() {
        return reduce;
    }

    public void setReduce(int reduce) {
        this.reduce = reduce;
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

    @Transient
    public String getCpname() {
        return cpname;
    }

    public void setCpname(String cpname) {
        this.cpname = cpname;
    }

    public int getCpid() {
        return cpid;
    }

    public void setCpid(int cpid) {
        this.cpid = cpid;
    }

    public String getCalled() {
        return called;
    }

    public void setCalled(String called) {
        this.called = called;
    }

    public String getBlackinfo() {
        return blackinfo;
    }

    public void setBlackinfo(String blackinfo) {
        this.blackinfo = blackinfo;
    }

    @Override
    public String toString() {
        return "CpNum{" +
                "cpid=" + cpid +
                ", called='" + called + '\'' +
                ", cpname='" + cpname + '\'' +
                ", daylimit=" + daylimit +
                ", monthlimit=" + monthlimit +
                ", reduce=" + reduce +
                ", blackinfo='" + blackinfo + '\'' +
                '}';
    }
}
