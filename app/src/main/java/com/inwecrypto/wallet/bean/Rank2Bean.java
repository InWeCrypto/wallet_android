package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xiaoji06 on 2018/4/2 15:14
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class Rank2Bean implements Serializable {


    /**
     * name : OEX
     * volume : 8842345.7729
     * volume_cny : 55771327.49341
     * type : {"zh":["现货","期货","OTC"],"en":["Actuals","Futures","OTC"]}
     * trade_ratio : 50
     * currency : 28
     * website : https://www.oex.com/
     */

    private String name;
    private String volume;
    private String volume_cny;
    private TypeBean type;
    private int trade_ratio;
    private int currency;
    private String website;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public TypeBean getType() {
        return type;
    }

    public void setType(TypeBean type) {
        this.type = type;
    }

    public int getTrade_ratio() {
        return trade_ratio;
    }

    public void setTrade_ratio(int trade_ratio) {
        this.trade_ratio = trade_ratio;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public static class TypeBean implements Serializable{
        private List<String> zh;
        private List<String> en;

        public List<String> getZh() {
            return zh;
        }

        public void setZh(List<String> zh) {
            this.zh = zh;
        }

        public List<String> getEn() {
            return en;
        }

        public void setEn(List<String> en) {
            this.en = en;
        }
    }
}
