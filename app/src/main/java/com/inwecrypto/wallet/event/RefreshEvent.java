package com.inwecrypto.wallet.event;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class RefreshEvent implements Serializable{
    private int type;
    private int position;
    private HashMap<String,String> params;

    public RefreshEvent(int type) {
        this.type = type;
    }

    public RefreshEvent(int type, HashMap<String, String> params) {
        this.type = type;
        this.params = params;
    }

    public RefreshEvent(int type, int position) {
        this.type = type;
        this.position = position;
    }

    public RefreshEvent(int type, int position, HashMap<String, String> params) {
        this.type = type;
        this.position = position;
        this.params = params;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }
}
