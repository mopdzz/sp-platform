package com.sp.platform.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: yfyanglei
 * Date: 13-4-6
 * Time: 下午10:19
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "tbl_spinfo")
public class SpInfo extends BaseEntity {
    private int opid;
    private String name;
    private String type;
    private String contact;
    private String syncurl;
    private String memo;
    private int status;

    public int getOpid() {
        return opid;
    }

    public void setOpid(int opid) {
        this.opid = opid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSyncurl() {
        return syncurl;
    }

    public void setSyncurl(String syncurl) {
        this.syncurl = syncurl;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SpInfo{" +
                "opid=" + opid +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", contact='" + contact + '\'' +
                ", syncurl='" + syncurl + '\'' +
                ", memo='" + memo + '\'' +
                ", status=" + status +
                '}';
    }
}
