package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/4/2 15:14
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class Rank4Bean implements Serializable {

    /**
     * category_id : 2
     * name : Neo
     * long_name : Neo
     * img :
     * industry : Blockchain
     * favorite : 12
     * search_rate : 12
     * click_rate : 3
     * score : 4
     * score_sum : 4
     * rank_score : 31
     * rank : 1
     */

    private int category_id;
    private String name;
    private String long_name;
    private String img;
    private String industry;
    private int favorite;
    private int search_rate;
    private int click_rate;
    private String score;
    private String score_sum;
    private int rank_score;
    private int rank;
    private ProjectDetaileBean category;

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

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getSearch_rate() {
        return search_rate;
    }

    public void setSearch_rate(int search_rate) {
        this.search_rate = search_rate;
    }

    public int getClick_rate() {
        return click_rate;
    }

    public void setClick_rate(int click_rate) {
        this.click_rate = click_rate;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScore_sum() {
        return score_sum;
    }

    public void setScore_sum(String score_sum) {
        this.score_sum = score_sum;
    }

    public int getRank_score() {
        return rank_score;
    }

    public void setRank_score(int rank_score) {
        this.rank_score = rank_score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public ProjectDetaileBean getCategory() {
        return category;
    }

    public void setCategory(ProjectDetaileBean category) {
        this.category = category;
    }
}
