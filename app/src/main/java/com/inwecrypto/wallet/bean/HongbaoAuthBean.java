package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/5/2 15:28
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class HongbaoAuthBean implements Serializable {

    /**
     * id : 30
     * auth_tx_id : 0x1c8b2af43c39d4213e4071f3ea837284d0b90c14a273f5da07793d75755c4f48
     * redbag_tx_id :
     * redbag_id : 0
     * redbag : 100
     * redbag_symbol : TNC-ETH
     * redbag_addr : 0x96973005c14d98d4e48aba0124537ccb6b5975b0
     * redbag_number : 3
     * fee :
     * fee_addr :
     * share_type : 0
     * share_attr :
     * share_user :
     * share_msg :
     * created_at : 2018-05-08 06:29:17
     * updated_at : 2018-05-08 06:29:17
     * share_theme_url :
     * fee_tx_id :
     * done : 0
     * auth_block : 3191070
     * redbag_block : 0
     * fee_block : 0
     * redbag_back_block : 0
     * redbag_back_tx_id :
     * redbag_back :
     * auth_at : 2018-05-08 06:29:17
     * redbag_at :
     * redbag_back_at :
     * redbag_back_tx_status : 0
     * status : -2
     * draw_redbag_number : 0
     * gnt_category : {"name":"TNC-ETH","icon":"http://inwecrypto-china.oss-cn-shanghai.aliyuncs.com/imgs/15222342851522238932252_ruid_rt_1c9m2s8ts1476rsr1p9m19kn1l1h1_name_logo.png","address":"0x6bf8c045ac5cc022568545997db24c946794c0c2","decimals":8,"gas":"420993"}
     */

    private String id;
    private String auth_tx_id;
    private String redbag_tx_id;
    private String redbag_id;
    private String redbag;
    private String redbag_symbol;
    private String redbag_addr;
    private int redbag_number;
    private String fee;
    private String fee_addr;
    private int share_type;
    private String share_attr;
    private String share_user;
    private String share_msg;
    private String created_at;
    private String updated_at;
    private String share_theme_url;
    private String fee_tx_id;
    private int done;
    private int auth_block;
    private int redbag_block;
    private int fee_block;
    private int redbag_back_block;
    private String redbag_back_tx_id;
    private String redbag_back;
    private String auth_at;
    private String redbag_at;
    private String redbag_back_at;
    private int redbag_back_tx_status;
    private int status;
    private int draw_redbag_number;
    private GntCategoryBean gnt_category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuth_tx_id() {
        return auth_tx_id;
    }

    public void setAuth_tx_id(String auth_tx_id) {
        this.auth_tx_id = auth_tx_id;
    }

    public String getRedbag_tx_id() {
        return redbag_tx_id;
    }

    public void setRedbag_tx_id(String redbag_tx_id) {
        this.redbag_tx_id = redbag_tx_id;
    }

    public String getRedbag_id() {
        return redbag_id;
    }

    public void setRedbag_id(String redbag_id) {
        this.redbag_id = redbag_id;
    }

    public String getRedbag() {
        return redbag;
    }

    public void setRedbag(String redbag) {
        this.redbag = redbag;
    }

    public String getRedbag_symbol() {
        return redbag_symbol;
    }

    public void setRedbag_symbol(String redbag_symbol) {
        this.redbag_symbol = redbag_symbol;
    }

    public String getRedbag_addr() {
        return redbag_addr;
    }

    public void setRedbag_addr(String redbag_addr) {
        this.redbag_addr = redbag_addr;
    }

    public int getRedbag_number() {
        return redbag_number;
    }

    public void setRedbag_number(int redbag_number) {
        this.redbag_number = redbag_number;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getFee_addr() {
        return fee_addr;
    }

    public void setFee_addr(String fee_addr) {
        this.fee_addr = fee_addr;
    }

    public int getShare_type() {
        return share_type;
    }

    public void setShare_type(int share_type) {
        this.share_type = share_type;
    }

    public String getShare_attr() {
        return share_attr;
    }

    public void setShare_attr(String share_attr) {
        this.share_attr = share_attr;
    }

    public String getShare_user() {
        return share_user;
    }

    public void setShare_user(String share_user) {
        this.share_user = share_user;
    }

    public String getShare_msg() {
        return share_msg;
    }

    public void setShare_msg(String share_msg) {
        this.share_msg = share_msg;
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

    public String getShare_theme_url() {
        return share_theme_url;
    }

    public void setShare_theme_url(String share_theme_url) {
        this.share_theme_url = share_theme_url;
    }

    public String getFee_tx_id() {
        return fee_tx_id;
    }

    public void setFee_tx_id(String fee_tx_id) {
        this.fee_tx_id = fee_tx_id;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public int getAuth_block() {
        return auth_block;
    }

    public void setAuth_block(int auth_block) {
        this.auth_block = auth_block;
    }

    public int getRedbag_block() {
        return redbag_block;
    }

    public void setRedbag_block(int redbag_block) {
        this.redbag_block = redbag_block;
    }

    public int getFee_block() {
        return fee_block;
    }

    public void setFee_block(int fee_block) {
        this.fee_block = fee_block;
    }

    public int getRedbag_back_block() {
        return redbag_back_block;
    }

    public void setRedbag_back_block(int redbag_back_block) {
        this.redbag_back_block = redbag_back_block;
    }

    public String getRedbag_back_tx_id() {
        return redbag_back_tx_id;
    }

    public void setRedbag_back_tx_id(String redbag_back_tx_id) {
        this.redbag_back_tx_id = redbag_back_tx_id;
    }

    public String getRedbag_back() {
        return redbag_back;
    }

    public void setRedbag_back(String redbag_back) {
        this.redbag_back = redbag_back;
    }

    public String getAuth_at() {
        return auth_at;
    }

    public void setAuth_at(String auth_at) {
        this.auth_at = auth_at;
    }

    public String getRedbag_at() {
        return redbag_at;
    }

    public void setRedbag_at(String redbag_at) {
        this.redbag_at = redbag_at;
    }

    public String getRedbag_back_at() {
        return redbag_back_at;
    }

    public void setRedbag_back_at(String redbag_back_at) {
        this.redbag_back_at = redbag_back_at;
    }

    public int getRedbag_back_tx_status() {
        return redbag_back_tx_status;
    }

    public void setRedbag_back_tx_status(int redbag_back_tx_status) {
        this.redbag_back_tx_status = redbag_back_tx_status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDraw_redbag_number() {
        return draw_redbag_number;
    }

    public void setDraw_redbag_number(int draw_redbag_number) {
        this.draw_redbag_number = draw_redbag_number;
    }

    public GntCategoryBean getGnt_category() {
        return gnt_category;
    }

    public void setGnt_category(GntCategoryBean gnt_category) {
        this.gnt_category = gnt_category;
    }

    public static class GntCategoryBean implements Serializable{
        /**
         * name : TNC-ETH
         * icon : http://inwecrypto-china.oss-cn-shanghai.aliyuncs.com/imgs/15222342851522238932252_ruid_rt_1c9m2s8ts1476rsr1p9m19kn1l1h1_name_logo.png
         * address : 0x6bf8c045ac5cc022568545997db24c946794c0c2
         * decimals : 8
         * gas : 420993
         */

        private String name;
        private String icon;
        private String address;
        private int decimals;
        private String gas;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getDecimals() {
            return decimals;
        }

        public void setDecimals(int decimals) {
            this.decimals = decimals;
        }

        public String getGas() {
            return gas;
        }

        public void setGas(String gas) {
            this.gas = gas;
        }
    }
}
