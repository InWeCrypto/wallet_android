package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/19.
 * 功能描述：
 * 版本：@version
 */

public class IcoGasBean implements Serializable {
    private String name;
    private String gas;
    private String icon;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
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
}
