package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2017/12/2 16:17
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class KLBean implements Serializable {

    /**
     * time : 1512169380000
     * end_time : 1512169439999
     * min_price : 456.01000000
     * max_price : 456.96000000
     * opened_price : 456.61000000
     * closed_price : 456.96000000
     * volume : 22.04892000
     */

    private long time;
    private long end_time;
    private String min_price;
    private String max_price;
    private String opened_price;
    private String closed_price;
    private String volume;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public String getOpened_price() {
        return opened_price;
    }

    public void setOpened_price(String opened_price) {
        this.opened_price = opened_price;
    }

    public String getClosed_price() {
        return closed_price;
    }

    public void setClosed_price(String closed_price) {
        this.closed_price = closed_price;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
}
