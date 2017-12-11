package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class MailBean implements Serializable {

    /**
     * id : 1
     * user_id : 78
     * ico_id : 6
     * name : name test
     * address : address test123
     * remark : null
     * created_at : 2017-11-23 03:48:37
     * updated_at : 2017-11-23 04:13:22
     */

    private int id;
    private int user_id;
    private int ico_id;
    private String name;
    private String address;
    private String remark;
    private String created_at;
    private String updated_at;
    private int headImg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getIco_id() {
        return ico_id;
    }

    public void setIco_id(int ico_id) {
        this.ico_id = ico_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getHeadImg() {
        return headImg;
    }

    public void setHeadImg(int headImg) {
        this.headImg = headImg;
    }
}
