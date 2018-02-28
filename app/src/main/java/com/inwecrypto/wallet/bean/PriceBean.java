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
     * price : 852.0106500000
     * volume : 5067429.7500000000
     * 24h_max_price : 856.2067500000
     * 24h_min_price : 851.6698500000
     * 24h_change : -0.44
     * price_cny : 5400.5547060900
     * volume_cny : 32120410.2133500000
     * 24h_max_price_cny : 5427.1521055500
     * 24h_min_price_cny : 5398.3945112100
     * 24h_change_cny : -0.44
     */

    private String price;
    private String volume;
    @SerializedName("24h_max_price")
    private String _$24h_max_price;
    @SerializedName("24h_min_price")
    private String _$24h_min_price;
    @SerializedName("24h_change")
    private String _$24h_change;
    private String price_cny;
    private String volume_cny;
    @SerializedName("24h_max_price_cny")
    private String _$24h_max_price_cny;
    @SerializedName("24h_min_price_cny")
    private String _$24h_min_price_cny;
    @SerializedName("24h_change_cny")
    private String _$24h_change_cny;

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

    public String getPrice_cny() {
        return price_cny;
    }

    public void setPrice_cny(String price_cny) {
        this.price_cny = price_cny;
    }

    public String getVolume_cny() {
        return volume_cny;
    }

    public void setVolume_cny(String volume_cny) {
        this.volume_cny = volume_cny;
    }

    public String get_$24h_max_price_cny() {
        return _$24h_max_price_cny;
    }

    public void set_$24h_max_price_cny(String _$24h_max_price_cny) {
        this._$24h_max_price_cny = _$24h_max_price_cny;
    }

    public String get_$24h_min_price_cny() {
        return _$24h_min_price_cny;
    }

    public void set_$24h_min_price_cny(String _$24h_min_price_cny) {
        this._$24h_min_price_cny = _$24h_min_price_cny;
    }

    public String get_$24h_change_cny() {
        return _$24h_change_cny;
    }

    public void set_$24h_change_cny(String _$24h_change_cny) {
        this._$24h_change_cny = _$24h_change_cny;
    }
}