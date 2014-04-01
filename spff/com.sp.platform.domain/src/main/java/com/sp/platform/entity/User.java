package com.sp.platform.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: mopdgg
 * Date: 13-3-30
 * Time: 下午10:07
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "tbl_user")
public class User extends BaseEntity {
    private String name; //姓名
    private String passwd; //密码
    private String showname; //显示名 默认=name
    private int parentId; //父用户ID
    /**
     * 角色 1：管理员 2：操作员 10：主帐号 11：子帐号
     */
    private int role;
    private String ip; //登陆IP地址
    private String contactMan;//联系人
    private String tel;//联系电话
    private String bankAccount; //银行帐号
    private int status; //状态

    /**
     * 同步地址， 子帐号默认等同于主帐号
     */
    private String syncurl;

    public String getSyncurl() {
        return syncurl;
    }

    public void setSyncurl(String syncurl) {
        this.syncurl = syncurl;
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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getContactMan() {
        return contactMan;
    }

    public void setContactMan(String contactMan) {
        this.contactMan = contactMan;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getShowname() {
        return showname;
    }

    public void setShowname(String showname) {
        this.showname = showname;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", passwd='" + passwd + '\'' +
                ", showname='" + showname + '\'' +
                ", parentId=" + parentId +
                ", role=" + role +
                ", ip='" + ip + '\'' +
                ", contactMan='" + contactMan + '\'' +
                ", tel='" + tel + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", status=" + status +
                ", syncurl='" + syncurl + '\'' +
                '}';
    }
}
