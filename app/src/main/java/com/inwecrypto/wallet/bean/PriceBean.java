package com.inwecrypto.wallet.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2017/12/5 14:37
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class PriceBean implements Serializable {

    /**
     * price : 11605.82000000
     * volume : 1592.89859400
     * 24h_max_price : 11660.00000000
     * 24h_min_price : 11447.68000000
     * 24h_change : 0
     */

    private String price;
    private String volume;
    @SerializedName("24h_max_price")
    private String _$24h_max_price;
    @SerializedName("24h_min_price")
    private String _$24h_min_price;
    @SerializedName("24h_change")
    private String _$24h_change;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String get_$24h_max_price() {
        return _$24h_max_price;
    }

    public void set_$24h_max_price(String _$24h_max_price) {
        this._$24h_max_price = _$24h_max_price;
    }

    public String get_$24h_min_price() {
        return _$24h_min_price;
    }

    public void set_$24h_min_price(String _$24h_min_price) {
        this._$24h_min_price = _$24h_min_price;
    }

    public String get_$24h_change() {
        return _$24h_change;
    }

    public void set_$24h_change(String _$24h_change) {
        this._$24h_change = _$24h_change;
    }
}
