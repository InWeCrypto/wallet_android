package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/2/8 11:39
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class CommonProjectBean implements Serializable {

    private int id;
    private int name;
    private String type;
    private boolean isShow;
    private int img;
    private boolean hasMessage;
    private String time;
    private String lastMessage;
    private boolean openTip=true;

    public CommonProjectBean(){

    }

    public CommonProjectBean(String type,boolean isShow){
        this.type=type;
        this.isShow=isShow;
    }

    public boolean isOpenTip() {
        return openTip;
    }

    public void setOpenTip(boolean openTip) {
        this.openTip = openTip;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean isHasMessage() {
        return hasMessage;
    }

    public void setHasMessage(boolean hasMessage) {
        this.hasMessage = hasMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
