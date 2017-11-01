package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/10.
 * 功能描述：
 * 版本：@version
 */

public class GntBean implements Serializable {

    /**
     * id : 2
     * category_id : 1
     * name : OMG
     * created_at : 2017-08-15 18:56:29
     * updated_at : 2017-08-17 00:22:25
     * icon : http://cryptobox.oss-cn-shenzhen.aliyuncs.com/94a4ee25-a651-3726-88ce-e3a8eebbcb86.png
     * address : 0x07a1e67129305b8a99c86a481681032c68f9f0f9
     * gas : 38319
     */

    private int id;
    private int category_id;
    private String name;
    private String created_at;
    private String updated_at;
    private String icon;
    private String address;
    private String gas;
    private boolean isSelect;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
