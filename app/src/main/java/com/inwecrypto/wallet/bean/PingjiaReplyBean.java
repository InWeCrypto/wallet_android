package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xiaoji06 on 2018/4/8 15:38
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class PingjiaReplyBean implements Serializable {

    /**
     * current_page : 1
     * data : [{"id":1,"category_id":2,"category_user_id":134,"user_id":41,"content":"test222222222222","created_at":"2018-03-28 05:34:17","user":{"id":41,"name":"啦啦啦","img":"http://inwecrypto-china.oss-cn-shanghai.aliyuncs.com/ios_header_1520655918.jpeg","email":"18081789190@163.com"}}]
     * first_page_url : /category/home_follow?page=1
     * from : 1
     * last_page : 1
     * last_page_url : /category/home_follow?page=1
     * next_page_url :
     * path : /category/home_follow
     * per_page : 3
     * prev_page_url :
     * to : 3
     * total : 3
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
         * id : 1
         * category_id : 2
         * category_user_id : 134
         * user_id : 41
         * content : test222222222222
         * created_at : 2018-03-28 05:34:17
         * user : {"id":41,"name":"啦啦啦","img":"http://inwecrypto-china.oss-cn-shanghai.aliyuncs.com/ios_header_1520655918.jpeg","email":"18081789190@163.com"}
         */

        private int id;
        private int category_id;
        private int category_user_id;
        private int user_id;
        private String content;
        private String created_at;
        private UserBean user;

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

        public int getCategory_user_id() {
            return category_user_id;
        }

        public void setCategory_user_id(int category_user_id) {
            this.category_user_id = category_user_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean implements Serializable {
            /**
             * id : 41
             * name : 啦啦啦
             * img : http://inwecrypto-china.oss-cn-shanghai.aliyuncs.com/ios_header_1520655918.jpeg
             * email : 18081789190@163.com
             */

            private int id;
            private String name;
            private String img;
            private String email;

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

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }
    }
}
