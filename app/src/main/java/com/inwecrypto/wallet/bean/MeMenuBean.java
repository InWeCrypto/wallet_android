package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/28.
 * 功能描述：
 * 版本：@version
 */

public class MeMenuBean implements Serializable {

    private String title;
    private int icon;
    private int type;

    public MeMenuBean() {
    }

    public MeMenuBean(String title, int icon, int type) {
        this.title = title;
        this.icon = icon;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
