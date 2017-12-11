package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/9.
 * 功能描述：
 * 版本：@version
 */

public class MarkeListBean implements Serializable {


    /**
     * id : 2
     * name : 菩提
     * long_name : 菩提
     * en_name : bodhi
     * en_long_name : bodhi
     * unit : BOT
     * img : https://whalewallet.oss-cn-hongkong.aliyuncs.com/ico_icon/project_icon_bodhi.png
     * web_site : http://www.bodhi.network/
     * desc : null
     * created_at : 2017-11-21 11:03:06
     * updated_at : 2017-11-21 11:03:07
     * enable : 1
     * sort : 0
     * key : bodhi
     * time_data : {"price_usd":0,"volume_usd_24h":0,"max_price_usd_24h":0,"min_price_usd_24h":0,"price_cny":0,"volume_cny_24h":0,"max_price_cny_24h":0,"min_price_cny_24h":0,"change_24h":0}
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
    private TimeDataBean time_data;

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

    public TimeDataBean getTime_data() {
        return time_data;
    }

    public void setTime_data(TimeDataBean time_data) {
        this.time_data = time_data;
    }

    public static class TimeDataBean implements Serializable{
        /**
         * price_usd : 0
         * volume_usd_24h : 0
         * max_price_usd_24h : 0
         * min_price_usd_24h : 0
         * price_cny : 0
         * volume_cny_24h : 0
         * max_price_cny_24h : 0
         * min_price_cny_24h : 0
         * change_24h : 0
         */

        private String price_usd;
        private String volume_usd_24h;
        private String max_price_usd_24h;
        private String min_price_usd_24h;
        private String price_cny;
        private String volume_cny_24h;
        private String max_price_cny_24h;
        private String min_price_cny_24h;
        private String change_24h;

        public String getPrice_usd() {
            return price_usd;
        }

        public void setPrice_usd(String price_usd) {
            this.price_usd = price_usd;
        }

        public String getVolume_usd_24h() {
            return volume_usd_24h;
        }

        public void setVolume_usd_24h(String volume_usd_24h) {
            this.volume_usd_24h = volume_usd_24h;
        }

        public String getMax_price_usd_24h() {
            return max_price_usd_24h;
        }

        public void setMax_price_usd_24h(String max_price_usd_24h) {
            this.max_price_usd_24h = max_price_usd_24h;
        }

        public String getMin_price_usd_24h() {
            return min_price_usd_24h;
        }

        public void setMin_price_usd_24h(String min_price_usd_24h) {
            this.min_price_usd_24h = min_price_usd_24h;
        }

        public String getPrice_cny() {
            return price_cny;
        }

        public void setPrice_cny(String price_cny) {
            this.price_cny = price_cny;
        }

        public String getVolume_cny_24h() {
            return volume_cny_24h;
        }

        public void setVolume_cny_24h(String volume_cny_24h) {
            this.volume_cny_24h = volume_cny_24h;
        }

        public String getMax_price_cny_24h() {
            return max_price_cny_24h;
        }

        public void setMax_price_cny_24h(String max_price_cny_24h) {
            this.max_price_cny_24h = max_price_cny_24h;
        }

        public String getMin_price_cny_24h() {
            return min_price_cny_24h;
        }

        public void setMin_price_cny_24h(String min_price_cny_24h) {
            this.min_price_cny_24h = min_price_cny_24h;
        }

        public String getChange_24h() {
            return change_24h;
        }

        public void setChange_24h(String change_24h) {
            this.change_24h = change_24h;
        }
    }
}
