package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/2/27 10:16
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class YaoqinBean implements Serializable {


    /**
     * code : nDGWvP
     * info : {"name":"what-00@qq.com","img":"","email":"what-00@qq.com"}
     */

    private String code;
    private boolean candy_bow_stat;
    private InfoBean info;

    public boolean isCandy_bow_stat() {
        return candy_bow_stat;
    }

    public void setCandy_bow_stat(boolean candy_bow_stat) {
        this.candy_bow_stat = candy_bow_stat;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean implements Serializable{
        /**
         * name : what-00@qq.com
         * img :
         * email : what-00@qq.com
         */

        private String name;
        private String img;
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
