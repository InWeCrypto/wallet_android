package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xiaoji06 on 2018/4/2 15:14
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class Rank5Bean implements Serializable {

    /**
     * current_page : 1
     * data : [{"author":"<null>","category":"<null>","category_id":0,"click_rate":10,"created_at":"2018-04-02 07:39:01","desc":12,"id":11,"img":"<null>","is_hot":0,"is_scroll":0,"is_sole":0,"is_top":0,"lang":"zh","title":123,"type":2,"updated_at":"2018-04-02 07:39:01","url":"<null>","video":"<null>"}]
     * first_page_url : http://dev.inwecrypto.com:4431/v2/article?page=1
     * from : 1
     * last_page : 1
     * last_page_url : http://dev.inwecrypto.com:4431/v2/article?page=1
     * next_page_url : <null>
     * path : http://dev.inwecrypto.com:4431/v2/article
     * per_page : 10
     * prev_page_url : <null>
     * to : 8
     * total : 8
     */

    private int current_page;
    private String first_page_url;
    private int from;
    private int last_page;
    private String last_page_url;
    private String next_page_url;
    private String path;
    private int per_page;
    private String prev_page_url;
    private int to;
    private int total;
    private List<DataBean> data;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public String getFirst_page_url() {
        return first_page_url;
    }

    public void setFirst_page_url(String first_page_url) {
        this.first_page_url = first_page_url;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public String getLast_page_url() {
        return last_page_url;
    }

    public void setLast_page_url(String last_page_url) {
        this.last_page_url = last_page_url;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(String prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        /**
         * id : 24
         * category_id : 10
         * type : 2
         * title : Trinity：团队及顾问
         * author : null
         * img : http://inwecrypto-china.oss-cn-shanghai.aliyuncs.com/ueditor/2ec8df939c80a9c94f8178686845437f.png
         * url : null
         * video : null
         * desc : 团队
         * lang : zh
         * is_hot : false
         * is_top : false
         * is_scroll : false
         * is_sole : false
         * created_at : 2018-01-28 15:14:00
         * updated_at : 2018-01-28 15:14:00
         * click_rate : 24
         * category : {"id":10,"name":"TNC","img":"http://inwecrypto-china.oss-cn-shanghai.aliyuncs.com/imgs/15197297194.png","type":1,"type_name":"交易中","industry":null}
         */

        private int id;
        private int category_id;
        private int type;
        private String title;
        private String author;
        private String img;
        private String url;
        private String video;
        private String desc;
        private String lang;
        private boolean is_hot;
        private boolean is_top;
        private boolean is_scroll;
        private boolean is_sole;
        private String created_at;
        private String updated_at;
        private int click_rate;
        private CategoryBean category;

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

        public int getClick_rate() {
            return click_rate;
        }

        public void setClick_rate(int click_rate) {
            this.click_rate = click_rate;
        }

        public CategoryBean getCategory() {
            return category;
        }

        public void setCategory(CategoryBean category) {
            this.category = category;
        }

        public static class CategoryBean implements Serializable{
            /**
             * id : 10
             * name : TNC
             * img : http://inwecrypto-china.oss-cn-shanghai.aliyuncs.com/imgs/15197297194.png
             * type : 1
             * type_name : 交易中
             * industry : null
             */

            private int id;
            private String name;
            private String img;
            private int type;
            private String type_name;
            private String industry;

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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getType_name() {
                return type_name;
            }

            public void setType_name(String type_name) {
                this.type_name = type_name;
            }

            public String getIndustry() {
                return industry;
            }

            public void setIndustry(String industry) {
                this.industry = industry;
            }
        }
    }
}
