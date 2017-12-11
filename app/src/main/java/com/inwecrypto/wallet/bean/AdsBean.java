package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xiaoji06 on 2017/11/14 15:15
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class AdsBean implements Serializable{

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean  implements Serializable{
        /**
         * id : 1
         * name : NEO
         * long_name :
         * en_name : null
         * img : img/ads/903863510342006659.png
         * icon : null
         * desc : Neo Develop Updates With InWeCrypt
         * url : null
         * url_desc : Read more
         * sort : 0
         * style : null
         * start_at : 2017-11-01 00:00:00
         * end_at : 2027-11-11 00:00:00
         * enable : 1
         * created_at : 2017-11-06 11:07:20
         * updated_at : null
         * url_icon : null
         * url_img : null
         * status : 1
         */

        private int id;
        private String name;
        private String long_name;
        private String en_name;
        private String img;
        private String icon;
        private String desc;
        private String url;
        private String url_desc;
        private int sort;
        private Object style;
        private String start_at;
        private String end_at;
        private int enable;
        private String created_at;
        private String updated_at;
        private String url_icon;
        private String url_img;
        private String status;

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

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl_desc() {
            return url_desc;
        }

        public void setUrl_desc(String url_desc) {
            this.url_desc = url_desc;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public Object getStyle() {
            return style;
        }

        public void setStyle(Object style) {
            this.style = style;
        }

        public String getStart_at() {
            return start_at;
        }

        public void setStart_at(String start_at) {
            this.start_at = start_at;
        }

        public String getEnd_at() {
            return end_at;
        }

        public void setEnd_at(String end_at) {
            this.end_at = end_at;
        }

        public int getEnable() {
            return enable;
        }

        public void setEnable(int enable) {
            this.enable = enable;
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

        public String getUrl_icon() {
            return url_icon;
        }

        public void setUrl_icon(String url_icon) {
            this.url_icon = url_icon;
        }

        public String getUrl_img() {
            return url_img;
        }

        public void setUrl_img(String url_img) {
            this.url_img = url_img;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
