package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/2/10 14:35
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ExchangeNoticeBean  implements Serializable {


    /**
     * id : 2
     * source_name : 币快讯 en
     * source_url : http://bishijie.com/111
     * content : 公告内容 en
     * url : null
     * lang : en
     * is_hot : false
     * is_top : false
     * is_scroll : false
     * created_at : 2018-01-16 17:47:00
     * updated_at : 2018-01-16 17:47:00
     */

    private int id;
    private String source_name;
    private String source_url;
    private String content;
    private String url;
    private String lang;
    private boolean is_hot;
    private boolean is_top;
    private boolean is_scroll;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isIs_hot() {
        return is_hot;
    }

    public void setIs_hot(boolean is_hot) {
        this.is_hot = is_hot;
    }

    public boolean isIs_top() {
        return is_top;
    }

    public void setIs_top(boolean is_top) {
        this.is_top = is_top;
    }

    public boolean isIs_scroll() {
        return is_scroll;
    }

    public void setIs_scroll(boolean is_scroll) {
        this.is_scroll = is_scroll;
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
}
