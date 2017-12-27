package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xiaoji06 on 2017/11/28 20:39
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NeoOderBean implements Serializable {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        /**
         * tx : 0x67905b068cde98d0450168bf6f8feac5eac390073a97cb660dadb056fa31ca11
         * from : AMpupnF6QweQXLfCtF4dR45FDdKbTXkLsr
         * to : AMpupnF6QweQXLfCtF4dR45FDdKbTXkLsr
         * asset : 0xc56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b
         * value : 1
         * createTime : 2017-11-26T22:38:16.133121Z
         * confirmTime : 2017-11-26T22:38:50.41296Z
         * remark:
         */

        private String tx;
        private String from;
        private String to;
        private String asset;
        private String value;
        private String createTime;
        private String confirmTime;
        private String remark;

        public String getTx() {
            return tx;
        }

        public void setTx(String tx) {
            this.tx = tx;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getAsset() {
            return asset;
        }

        public void setAsset(String asset) {
            this.asset = asset;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getConfirmTime() {
            return confirmTime;
        }

        public void setConfirmTime(String confirmTime) {
            this.confirmTime = confirmTime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
