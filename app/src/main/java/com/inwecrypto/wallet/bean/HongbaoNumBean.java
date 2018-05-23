package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/5/3 11:10
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class HongbaoNumBean implements Serializable {

    /**
     * all : 12
     * fail : 1
     * success : 5
     * create : 4
     * send : 2
     */

    private int all;
    private int fail;
    private int success;
    private int create;
    private int send;

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }

    public int getFail() {
        return fail;
    }

    public void setFail(int fail) {
        this.fail = fail;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getCreate() {
        return create;
    }

    public void setCreate(int create) {
        this.create = create;
    }

    public int getSend() {
        return send;
    }

    public void setSend(int send) {
        this.send = send;
    }
}
