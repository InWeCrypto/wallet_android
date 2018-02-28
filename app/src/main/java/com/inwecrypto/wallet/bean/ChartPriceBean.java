package com.inwecrypto.wallet.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/2/23 17:40
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ChartPriceBean implements Serializable {

    /**
     * price : 829.1954632500
     * volume : 2327681.6062500000
     * 24h_max_price : 829.1954632500
     * 24h_min_price : 777.5309195500
     * 24h_change : 6.63
     * price_cny : 5266.2203871007
     * volume_cny : 14783105.8812937500
     * 24h_max_price_cny : 5266.2203871007
     * 24h_min_price_cny : 4938.0988700620
     * 24h_change_cny : 6.63
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
