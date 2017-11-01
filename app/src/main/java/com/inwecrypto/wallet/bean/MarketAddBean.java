package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 * 功能描述：
 * 版本：@version
 */

public class MarketAddBean implements Serializable {

    /**
     * name : coinmarket
     * data : [{"id":3,"name":"NEO","flag":"NEO","token":null,"url":null,"created_at":"2017-08-14 17:45:42","updated_at":"2017-08-14 17:45:42","source":"coinmarket","icon":null,"relation_user_count":0}]
     */

    private String name;
    private List<DataBean> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 3
         * name : NEO
         * flag : NEO
         * token : null
         * url : null
         * created_at : 2017-08-14 17:45:42
         * updated_at : 2017-08-14 17:45:42
         * source : coinmarket
         * icon : null
         * relation_user_count : 0
         */

        private int id;
        private String name;
        private String flag;
        private String token;
        private String url;
        private String created_at;
        private String updated_at;
        private String source;
        private String icon;
        private int relation_user_count;
        private int type;

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

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
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

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getRelation_user_count() {
            return relation_user_count;
        }

        public void setRelation_user_count(int relation_user_count) {
            this.relation_user_count = relation_user_count;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
