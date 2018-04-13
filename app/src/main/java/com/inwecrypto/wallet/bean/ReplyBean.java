package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/4/9 17:47
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ReplyBean implements Serializable {


    /**
     * category_id : 2
     * category_user_id : 134
     * user_id : 29
     * updated_at : 2018-03-29 06:04:22
     * created_at : 2018-03-29 06:04:22
     * id : 2
     * up : 1
     * down : 0
     * equal : 0
     */

    private String category_id;
    private String category_user_id;
    private int user_id;
    private String updated_at;
    private String created_at;
    private int id;
    private int up;
    private int down;
    private int equal;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_user_id() {
        return category_user_id;
    }

    public void setCategory_user_id(String category_user_id) {
        this.category_user_id = category_user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getEqual() {
        return equal;
    }

    public void setEqual(int equal) {
        this.equal = equal;
    }
}
