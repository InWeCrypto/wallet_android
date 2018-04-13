package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/4/8 17:46
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectAnalysisBean implements Serializable {

    /**
     * very_dissatisfied : 1
     * dissatisfied : 0
     * good : 0
     * recommend : 1
     * like : 1
     * score_avg :
     * total : 3
     */

    private int very_dissatisfied;
    private int dissatisfied;
    private int good;
    private int recommend;
    private int like;
    private String score_avg;
    private int total;

    public int getVery_dissatisfied() {
        return very_dissatisfied;
    }

    public void setVery_dissatisfied(int very_dissatisfied) {
        this.very_dissatisfied = very_dissatisfied;
    }

    public int getDissatisfied() {
        return dissatisfied;
    }

    public void setDissatisfied(int dissatisfied) {
        this.dissatisfied = dissatisfied;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getScore_avg() {
        return score_avg;
    }

    public void setScore_avg(String score_avg) {
        this.score_avg = score_avg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
