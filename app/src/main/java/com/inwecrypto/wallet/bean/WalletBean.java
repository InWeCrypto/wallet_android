package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/15.
 * 功能描述：
 * 版本：@version
 */

public class WalletBean implements Serializable{


    /**
     * id : 90
     * user_id : 8
     * category_id : 2
     * name : 有钱Neo
     * address : AMpupnF6QweQXLfCtF4dR45FDdKbTXkLsr
     * created_at : 2017-11-27 18:16:14
     * updated_at : 2017-11-27 18:16:14
     * deleted_at : null
     * category : {"id":2,"name":"NEO"}
     */

    private int id;
    private int user_id;
    private int category_id;
    private String name;
    private String address;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private int icon;
    private String type;
    private CategoryBean category;
    private String token_name;
    private String token_num;
    private String token_decimal;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getToken_name() {
        return token_name;
    }

    public void setToken_name(String token_name) {
        this.token_name = token_name;
    }

    public String getToken_num() {
        return token_num;
    }

    public void setToken_num(String token_num) {
        this.token_num = token_num;
    }

    public String getToken_decimal() {
        return token_decimal;
    }

    public void setToken_decimal(String token_decimal) {
        this.token_decimal = token_decimal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public CategoryBean getCategory() {
        return category;
    }

    public void setCategory(CategoryBean category) {
        this.category = category;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class CategoryBean implements Serializable{
        /**
         * id : 2
         * name : NEO
         */

        private int id;
        private String name;

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
    }
}
