package com.inwecrypto.wallet.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/5/11 17:18
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class HongbaoMinGas implements Serializable {


    /**
     * p_id : 0
     * key : REDBAG_MAX_GAS
     * value : 0.00010800
     * comment : 红包最小GAS
     * private : 0
     * created_at : 2018-03-13 14:57:00
     * updated_at : 2018-03-13 14:57:00
     */

    private int p_id;
    private String key;
    private String value;
    private String comment;
    private String created_at;
    private String updated_at;

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
}
