package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2017/11/17 11:49
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class InfoMarketBean implements Serializable{


    /**
     * sort : 3
     * source : Bittrex
     * pair : QTUM/BTC
     * volum_24 : $10,914,000
     * pairce : $12.63
     * volum_percent : 5.52%
     * update : Recently
     */

    private String sort;
    private String source;
    private String pair;
    private String volum_24;
    private String pairce;
    private String volum_percent;
    private String update;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getVolum_24() {
        return volum_24;
    }

    public void setVolum_24(String volum_24) {
        this.volum_24 = volum_24;
    }

    public String getPairce() {
        return pairce;
    }

    public void setPairce(String pairce) {
        this.pairce = pairce;
    }

    public String getVolum_percent() {
        return volum_percent;
    }

    public void setVolum_percent(String volum_percent) {
        this.volum_percent = volum_percent;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }
}
