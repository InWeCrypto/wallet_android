package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/5/3 10:32
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class HongbaoOrderBean implements Serializable {


    /**
     * tx : 0x4693f0d087b966c596fac2487939cbb9885936149e9c58fa26c45af81917ede4
     * asset : 0x6bf8c045ac5cc022568545997db24c946794c0c2
     * from : 0x96973005c14d98d4e48aba0124537ccb6b5975b0
     * to : 0x6bf8c045ac5cc022568545997db24c946794c0c2
     * value : 0x2540be400
     * blocks : 3158305
     * context :
     */

    private String tx;
    private String asset;
    private String from;
    private String to;
    private String value;
    private int blocks;
    private String context;

    public String getTx() {
        return tx;
    }

    public void setTx(String tx) {
        this.tx = tx;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getBlocks() {
        return blocks;
    }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
