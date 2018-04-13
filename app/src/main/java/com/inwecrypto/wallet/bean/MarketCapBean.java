package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/4/10 17:09
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class MarketCapBean implements Serializable {


    /**
     * name : Bitcoin
     * name_zh : 比特币
     * symbol : BTC
     * img :
     * rank : 1
     * change : -6.05%
     * price : 7190
     * price_cny : 45178
     * volume : 7850510452
     * volume_cny : 49330297072
     * high_price_cny : 48242
     * low_price_cny : 42282
     * high_price : 7681.84713
     * low_price : 6732.80255
     * market : 121847576514
     * market_cny : 765653616539
     * liquidity : 16947437
     * circulation : 21000000
     * category : {}
     */

    private String name;
    private String name_zh;
    private String symbol;
    private String img;
    private int rank;
    private String change;
    private String price;
    private String price_cny;
    private String volume;
    private String volume_cny;
    private String high_price_cny;
    private String low_price_cny;
    private String high_price;
    private String low_price;
    private String market;
    private String market_cny;
    private String liquidity;
    private String circulation;
    private ProjectDetaileBean category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_zh() {
        return name_zh;
    }

    public void setName_zh(String name_zh) {
        this.name_zh = name_zh;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
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

    public String getHigh_price_cny() {
        return high_price_cny;
    }

    public void setHigh_price_cny(String high_price_cny) {
        this.high_price_cny = high_price_cny;
    }

    public String getLow_price_cny() {
        return low_price_cny;
    }

    public void setLow_price_cny(String low_price_cny) {
        this.low_price_cny = low_price_cny;
    }

    public String getHigh_price() {
        return high_price;
    }

    public void setHigh_price(String high_price) {
        this.high_price = high_price;
    }

    public String getLow_price() {
        return low_price;
    }

    public void setLow_price(String low_price) {
        this.low_price = low_price;
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

    public String getLiquidity() {
        return liquidity;
    }

    public void setLiquidity(String liquidity) {
        this.liquidity = liquidity;
    }

    public String getCirculation() {
        return circulation;
    }

    public void setCirculation(String circulation) {
        this.circulation = circulation;
    }

    public ProjectDetaileBean getCategory() {
        return category;
    }

    public void setCategory(ProjectDetaileBean category) {
        this.category = category;
    }

    public static class CategoryBean {
    }
}
