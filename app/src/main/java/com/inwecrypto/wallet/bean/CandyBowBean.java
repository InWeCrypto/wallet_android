package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/10 16:22
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class CandyBowBean implements Serializable {

    /**
     * list : {"current_page":1,"data":[{"id":2,"category_id":7,"name":"qtum 空投测试 en","img":"","desc":"qtum 空投测试 内容 en","url":"","year":2018,"month":1,"day":16,"is_scroll":0,"lang":"en"}],"first_page_url":"/candy_bow?page=1","from":1,"last_page":1,"last_page_url":"/candy_bow?page=1","next_page_url":"","path":"/candy_bow","per_page":10,"prev_page_url":"","to":1,"total":1}
     * candy_bow_user : true
     * is_read : true
     */

    private ListBean list;
    private boolean candy_bow_user;
    private boolean is_read;

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public boolean isCandy_bow_user() {
        return candy_bow_user;
    }

    public void setCandy_bow_user(boolean candy_bow_user) {
        this.candy_bow_user = candy_bow_user;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public static class ListBean implements Serializable{
        /**
         * current_page : 1
         * data : [{"id":2,"category_id":7,"name":"qtum 空投测试 en","img":"","desc":"qtum 空投测试 内容 en","url":"","year":2018,"month":1,"day":16,"is_scroll":0,"lang":"en"}]
         * first_page_url : /candy_bow?page=1
         * from : 1
         * last_page : 1
         * last_page_url : /candy_bow?page=1
         * next_page_url :
         * path : /candy_bow
         * per_page : 10
         * prev_page_url :
         * to : 1
         * total : 1
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

        public static class DataBean implements Serializable{
            /**
             * id : 2
             * category_id : 7
             * name : qtum 空投测试 en
             * img :
             * desc : qtum 空投测试 内容 en
             * url :
             * year : 2018
             * month : 1
             * day : 16
             * is_scroll : 0
             * lang : en
             */

            private int id;
            private int category_id;
            private String name;
            private String img;
            private String desc;
            private String url;
            private int year;
            private int month;
            private int day;
            private int is_scroll;
            private String lang;

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

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getIs_scroll() {
                return is_scroll;
            }

            public void setIs_scroll(int is_scroll) {
                this.is_scroll = is_scroll;
            }

            public String getLang() {
                return lang;
            }

            public void setLang(String lang) {
                this.lang = lang;
            }
        }
    }
}
