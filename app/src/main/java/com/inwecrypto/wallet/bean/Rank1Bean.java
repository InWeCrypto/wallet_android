package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/4/2 15:14
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class Rank1Bean implements Serializable {

    /**
     * rank : 1
     * key : bitcoin
     * symbol : BTC
     * img :
     * price : 7170
     * price_cny : 45052
     * volume : 7832231293.87909
     * volume_cny : 49215436325.826
     * change : -6.31%
     * market : 120479639966
     * market_cny : 757057913652
     */

    private String rank;
    private String key;
    private String symbol;
    private String img;
    private String price;
    private String price_cny;
    private String volume;
    private String volume_cny;
    private String change;
    private String market;
    private String market_cny;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_cny() {
        return price_cny;
    }

    public void setPrice_cny(String price_cny) {
        this.price_cny = price_cny;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getVolume_cny() {
        return volume_cny;
    }

    public void setVolume_cny(String volume_cny) {
        this.volume_cny = volume_cny;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getMarket_cny() {
        return market_cny;
    }

    public void setMarket_cny(String market_cny) {
        this.market_cny = market_cny;
    }
}
