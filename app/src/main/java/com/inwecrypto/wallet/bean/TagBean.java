package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/4/9 14:26
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class TagBean implements Serializable {

    /**
     * key : negative
     * name : 不推荐
     * number : 0
     */

    private String key;
    private String name;
    private int number;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
