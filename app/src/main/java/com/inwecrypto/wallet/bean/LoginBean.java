package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 * 功能描述：
 * 版本：@version
 */

public class LoginBean implements Serializable {


    /**
     * id : 42
     * name : 171778415@qq.com
     * img :
     * email : 171778415@qq.com
     * lang : zh
     * enable : true
     * wallet_gnt_sort : ["NEO","TNC","Gas","ETH"]
     * token : Bearer
     */

    private int id;
    private String name;
    private String img;
    private String email;
    private String lang;
    private boolean enable;
    private String token;
    private List<String> wallet_gnt_sort;

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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getWallet_gnt_sort() {
        return wallet_gnt_sort;
    }

    public void setWallet_gnt_sort(List<String> wallet_gnt_sort) {
        this.wallet_gnt_sort = wallet_gnt_sort;
    }
}
