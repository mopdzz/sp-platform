package com.sp.platform.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: yangl
 * Date: 13-6-23 下午3:25
 */
@Entity
@Table(name = "tbl_noprovice_code")
public class NaHaoduan{
    private Integer id;
    private String caller;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }
}
