package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/4/27 11:04
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class HongbaoGntBean implements Serializable {


    /**
     * name :
     * icon :
     * address : 0x6bf8c045ac5cc022568545997db24c946794c0c2
     * gas : 420993
     * decimals : 8
     */

    private String name;
    private String icon;
    private String address;
    private String gas;
    private String decimals;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getDecimals() {
        return decimals;
    }

    public void setDecimals(String decimals) {
        this.decimals = decimals;
    }
}
