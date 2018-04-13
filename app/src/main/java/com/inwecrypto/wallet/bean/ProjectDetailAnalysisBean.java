package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/4/8 18:25
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectDetailAnalysisBean implements Serializable {

    /**
     * date_range : 2018-02-08
     * very_dissatisfied : 1
     * like : 0
     */

    private String date_range;
    private int very_dissatisfied;
    private int like;

    public String getDate_range() {
        return date_range;
    }

    public void setDate_range(String date_range) {
        this.date_range = date_range;
    }

    public int getVery_dissatisfied() {
        return very_dissatisfied;
    }

    public void setVery_dissatisfied(int very_dissatisfied) {
        this.very_dissatisfied = very_dissatisfied;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
