package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/1.
 * 功能描述：
 * 版本：@version
 */

public class LoginBean implements Serializable {

    /**
     * id : 18
     * name : 171778415@qq.com
     * img : null
     * email : 171778415@qq.com
     * lang : zh
     * token : Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjE4LCJpc3MiOiJodHRwOi8vY2hpbmEuaW53ZWNyeXB0by5jb206NDQzMS92Mi9sb2dpbiIsImlhdCI6MTUxNzkxMDc3NSwiZXhwIjoxNTE3OTQ2Nzc1LCJuYmYiOjE1MTc5MTA3NzUsImp0aSI6Ilp2V2JFU21nenQxQTIxUU8ifQ.H_3MewaIGUNfEgRpnDF1qPzUWlOdcrZ3vgyciwsyX3M
     */

    private int id;
    private String name;
    private String img;
    private String email;
    private String lang;
    private String token;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
