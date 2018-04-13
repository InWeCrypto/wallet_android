package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xiaoji06 on 2018/4/8 15:18
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class PingjiaInfoBean implements Serializable {

    /**
     * id : 134
     * category_id : 2
     * user_id : 41
     * is_favorite : false
     * is_market_follow : false
     * market_hige : 0
     * market_lost : 0
     * score : 1
     * is_favorite_dot : false
     * is_top : true
     * is_category_comment : 1
     * category_comment_tag_id : 2
     * category_comment_tag_name : 最新
     * category_comment : test
     * category_comment_at : 2018-03-28 02:16:08
     * user_click_comment_up_count : 0
     * user_click_comment_down_count : 1
     * user_click_comment_equal_count : 0
     * comment_count : 9
     * user : {"id":41,"name":"啦啦啦","img":"","email":"18081789190@163.com"}
     * user_click_comment : [{"category_user_id":134,"up":0,"down":1,"equal":0}]
     */

    private int id;
    private int category_id;
    private int user_id;
    private boolean is_favorite;
    private boolean is_market_follow;
    private String market_hige;
    private String market_lost;
    private String score;
    private boolean is_favorite_dot;
    private boolean is_top;
    private int is_category_comment;
    private int category_comment_tag_id;
    private String category_comment_tag_name;
    private String category_comment;
    private String category_comment_at;
    private int user_click_comment_up_count;
    private int user_click_comment_down_count;
    private int user_click_comment_equal_count;
    private int comment_count;
    private UserBean user;
    private List<UserClickCommentBean> user_click_comment;

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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean isIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

    public boolean isIs_market_follow() {
        return is_market_follow;
    }

    public void setIs_market_follow(boolean is_market_follow) {
        this.is_market_follow = is_market_follow;
    }

    public String getMarket_hige() {
        return market_hige;
    }

    public void setMarket_hige(String market_hige) {
        this.market_hige = market_hige;
    }

    public String getMarket_lost() {
        return market_lost;
    }

    public void setMarket_lost(String market_lost) {
        this.market_lost = market_lost;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public boolean isIs_favorite_dot() {
        return is_favorite_dot;
    }

    public void setIs_favorite_dot(boolean is_favorite_dot) {
        this.is_favorite_dot = is_favorite_dot;
    }

    public boolean isIs_top() {
        return is_top;
    }

    public void setIs_top(boolean is_top) {
        this.is_top = is_top;
    }

    public int getIs_category_comment() {
        return is_category_comment;
    }

    public void setIs_category_comment(int is_category_comment) {
        this.is_category_comment = is_category_comment;
    }

    public int getCategory_comment_tag_id() {
        return category_comment_tag_id;
    }

    public void setCategory_comment_tag_id(int category_comment_tag_id) {
        this.category_comment_tag_id = category_comment_tag_id;
    }

    public String getCategory_comment_tag_name() {
        return category_comment_tag_name;
    }

    public void setCategory_comment_tag_name(String category_comment_tag_name) {
        this.category_comment_tag_name = category_comment_tag_name;
    }

    public String getCategory_comment() {
        return category_comment;
    }

    public void setCategory_comment(String category_comment) {
        this.category_comment = category_comment;
    }

    public String getCategory_comment_at() {
        return category_comment_at;
    }

    public void setCategory_comment_at(String category_comment_at) {
        this.category_comment_at = category_comment_at;
    }

    public int getUser_click_comment_up_count() {
        return user_click_comment_up_count;
    }

    public void setUser_click_comment_up_count(int user_click_comment_up_count) {
        this.user_click_comment_up_count = user_click_comment_up_count;
    }

    public int getUser_click_comment_down_count() {
        return user_click_comment_down_count;
    }

    public void setUser_click_comment_down_count(int user_click_comment_down_count) {
        this.user_click_comment_down_count = user_click_comment_down_count;
    }

    public int getUser_click_comment_equal_count() {
        return user_click_comment_equal_count;
    }

    public void setUser_click_comment_equal_count(int user_click_comment_equal_count) {
        this.user_click_comment_equal_count = user_click_comment_equal_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<UserClickCommentBean> getUser_click_comment() {
        return user_click_comment;
    }

    public void setUser_click_comment(List<UserClickCommentBean> user_click_comment) {
        this.user_click_comment = user_click_comment;
    }

    public static class UserBean implements Serializable {
        /**
         * id : 41
         * name : 啦啦啦
         * img :
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

    public static class UserClickCommentBean implements Serializable {
        /**
         * category_user_id : 134
         * up : 0
         * down : 1
         * equal : 0
         */

        private int category_user_id;
        private int up;
        private int down;
        private int equal;

        public int getCategory_user_id() {
            return category_user_id;
        }

        public void setCategory_user_id(int category_user_id) {
            this.category_user_id = category_user_id;
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
}
