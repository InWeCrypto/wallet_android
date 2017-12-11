package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/15.
 * 功能描述：
 * 版本：@version
 */

public class MarketAddBean implements Serializable {

    /**
     * id : 1
     * name : 量子链
     * long_name : 量子链
     * en_name : qtum
     * en_long_name : qtum
     * unit : QTUM
     * img : https://whalewallet.oss-cn-hongkong.aliyuncs.com/ico_icon/project_icon_Qtum.png
     * web_site : https://qtum.org
     * desc : null
     * created_at : 2017-11-21 11:03:06
     * updated_at : 2017-11-21 11:03:07
     * enable : 1
     * sort : 0
     * key : qtum
     * user_ticker : {"ico_id":1,"sort":0}
     */

    private int id;
    private String name;
    private String long_name;
    private String en_name;
    private String en_long_name;
    private String unit;
    private String img;
    private String web_site;
    private String desc;
    private String created_at;
    private String updated_at;
    private int enable;
    private int sort;
    private String key;
    private UserTickerBean user_ticker;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public String getEn_long_name() {
        return en_long_name;
    }

    public void setEn_long_name(String en_long_name) {
        this.en_long_name = en_long_name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getWeb_site() {
        return web_site;
    }

    public void setWeb_site(String web_site) {
        this.web_site = web_site;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public UserTickerBean getUser_ticker() {
        return user_ticker;
    }

    public void setUser_ticker(UserTickerBean user_ticker) {
        this.user_ticker = user_ticker;
    }

    public static class UserTickerBean implements Serializable{
        /**
         * ico_id : 1
         * sort : 0
         */

        private int ico_id;
        private int sort;

        public int getIco_id() {
            return ico_id;
        }

        public void setIco_id(int ico_id) {
            this.ico_id = ico_id;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }
    }
}
