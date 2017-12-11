package com.inwecrypto.wallet.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2017/11/17 11:30
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class InfoPriceBean implements Serializable {

    /**
     * id : neo
     * name : NEO
     * symbol : NEO
     * price : 41.6407
     * volume : 283884000.0
     * 24h_change : -2.22
     * 24h_max_price : 42.9168
     * 24h_min_price : 41.7782
     */

    private String id;
    private String name;
    private String symbol;
    private String price;
    private String volume;
    @SerializedName("24h_change")
    private String _$24h_change;
    @SerializedName("24h_max_price")
    private String _$24h_max_price;
    @SerializedName("24h_min_price")
    private String _$24h_min_price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

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

    public String get_$24h_change() {
        return _$24h_change;
    }

    public void set_$24h_change(String _$24h_change) {
        this._$24h_change = _$24h_change;
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
}
