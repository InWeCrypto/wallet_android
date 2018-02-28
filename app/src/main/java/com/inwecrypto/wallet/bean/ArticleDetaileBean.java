package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/2/7 16:36
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ArticleDetaileBean implements Serializable {

    /**
     * id : 2
     * category_id : 7
     * type : 1
     * title : 文本 en
     * author : 作者
     * img :
     * url :
     * video :
     * desc : 文本描述
     * sort : 0
     * click_rate : 3
     * lang : en
     * is_hot : false
     * is_top : false
     * is_scroll : false
     * is_sole : false
     * created_at : 2018-01-14 20:42:00
     * updated_at : 2018-01-14 20:42:00
     * article_user : {"user_id":5}
     * category : {"id":7,"name":"Qtum","img":"","type":"Trading"}
     */

    private String id;
    private String category_id;
    private int type;
    private String title;
    private String author;
    private String img;
    private String url;
    private String video;
    private String desc;
    private int sort;
    private int click_rate;
    private String lang;
    private boolean is_hot;
    private boolean is_top;
    private boolean is_scroll;
    private boolean is_sole;
    private String created_at;
    private String updated_at;
    private ArticleUserBean article_user;
    private CategoryBean category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getClick_rate() {
        return click_rate;
    }

    public void setClick_rate(int click_rate) {
        this.click_rate = click_rate;
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

    public boolean isIs_sole() {
        return is_sole;
    }

    public void setIs_sole(boolean is_sole) {
        this.is_sole = is_sole;
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

    public ArticleUserBean getArticle_user() {
        return article_user;
    }

    public void setArticle_user(ArticleUserBean article_user) {
        this.article_user = article_user;
    }

    public CategoryBean getCategory() {
        return category;
    }

    public void setCategory(CategoryBean category) {
        this.category = category;
    }

    public static class ArticleUserBean implements Serializable{
        /**
         * user_id : 5
         */

        private int user_id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }

    public static class CategoryBean implements Serializable{
        /**
         * id : 7
         * name : Qtum
         * img :
         * type : Trading
         */

        private int id;
        private String name;
        private String img;
        private String type;

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

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
