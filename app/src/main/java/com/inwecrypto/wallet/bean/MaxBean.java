package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/5/2 18:59
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class MaxBean implements Serializable {
    private String to;
    private String data;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
