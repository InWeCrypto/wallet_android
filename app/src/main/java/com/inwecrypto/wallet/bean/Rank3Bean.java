package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/4/2 15:14
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class Rank3Bean implements Serializable {

    /**
     * id : 165
     * balance : 23695.13
     * author : AuroraDAO
     * disabled : null
     * title : IDEX
     * description : IDEX - Decentralized Exchange
     * url : https://idex.market
     * category : other
     * contractsCount : 1
     * featured : 0
     * volumeLastDay : 6682.56
     * volumeLastWeek : 44180.88
     * txLastDay : 14679
     * txLastWeek : 105685
     * dauLastDay : 2702
     * updatedAt : 2018-03-26 07:57:05
     * createdAt : 2018-02-26 08:38:43
     * nsfw : 0
     * slug : idex
     */

    private String id;
    private String balance;
    private String author;
    private Object disabled;
    private String title;
    private String description;
    private String url;
    private String category;
    private String contractsCount;
    private String featured;
    private String volumeLastDay;
    private String volumeLastWeek;
    private String txLastDay;
    private String txLastWeek;
    private String dauLastDay;
    private String updatedAt;
    private String createdAt;
    private String nsfw;
    private String slug;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Object getDisabled() {
        return disabled;
    }

    public void setDisabled(Object disabled) {
        this.disabled = disabled;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContractsCount() {
        return contractsCount;
    }

    public void setContractsCount(String contractsCount) {
        this.contractsCount = contractsCount;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public String getVolumeLastDay() {
        return volumeLastDay;
    }

    public void setVolumeLastDay(String volumeLastDay) {
        this.volumeLastDay = volumeLastDay;
    }

    public String getVolumeLastWeek() {
        return volumeLastWeek;
    }

    public void setVolumeLastWeek(String volumeLastWeek) {
        this.volumeLastWeek = volumeLastWeek;
    }

    public String getTxLastDay() {
        return txLastDay;
    }

    public void setTxLastDay(String txLastDay) {
        this.txLastDay = txLastDay;
    }

    public String getTxLastWeek() {
        return txLastWeek;
    }

    public void setTxLastWeek(String txLastWeek) {
        this.txLastWeek = txLastWeek;
    }

    public String getDauLastDay() {
        return dauLastDay;
    }

    public void setDauLastDay(String dauLastDay) {
        this.dauLastDay = dauLastDay;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getNsfw() {
        return nsfw;
    }

    public void setNsfw(String nsfw) {
        this.nsfw = nsfw;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
