package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/4/13 14:43
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class MarketPriceBean implements Serializable {

    /**
     * bitcoin_percentage_of_market_cap : 42.590388068205826
     * active_cryptocurrencies : 1565
     * total_volume_usd : 2.5620502004222404E10
     * active_markets : 10245
     * total_market_cap_by_available_supply_usd : 3.1027575996343085E11
     */

    private double bitcoin_percentage_of_market_cap;
    private int active_cryptocurrencies;
    private String total_volume_usd;
    private int active_markets;
    private String total_market_cap_by_available_supply_usd;

    public double getBitcoin_percentage_of_market_cap() {
        return bitcoin_percentage_of_market_cap;
    }

    public void setBitcoin_percentage_of_market_cap(double bitcoin_percentage_of_market_cap) {
        this.bitcoin_percentage_of_market_cap = bitcoin_percentage_of_market_cap;
    }

    public int getActive_cryptocurrencies() {
        return active_cryptocurrencies;
    }

    public void setActive_cryptocurrencies(int active_cryptocurrencies) {
        this.active_cryptocurrencies = active_cryptocurrencies;
    }

    public String getTotal_volume_usd() {
        return total_volume_usd;
    }

    public void setTotal_volume_usd(String total_volume_usd) {
        this.total_volume_usd = total_volume_usd;
    }

    public int getActive_markets() {
        return active_markets;
    }

    public void setActive_markets(int active_markets) {
        this.active_markets = active_markets;
    }

    public String getTotal_market_cap_by_available_supply_usd() {
        return total_market_cap_by_available_supply_usd;
    }

    public void setTotal_market_cap_by_available_supply_usd(String total_market_cap_by_available_supply_usd) {
        this.total_market_cap_by_available_supply_usd = total_market_cap_by_available_supply_usd;
    }
}
