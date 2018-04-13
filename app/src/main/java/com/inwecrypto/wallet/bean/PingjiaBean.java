package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xiaoji06 on 2018/4/4 15:15
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class PingjiaBean implements Serializable{

    /**
     * id : 1
     * category_id : 2
     * user_id : 17
     * is_favorite : false
     * is_market_follow : false
     * is_favorite_dot : false
     * is_top : false
     * market_hige : 0
     * market_lost : 0
     * score : 5
     * is_category_comment : 1
     * category_comment_tag_id : 7
     * category_comment : 测试存储成三生三世超出搜索
     * category_comment_at : 2018-03-29 03:29:09
     * category_comment_tag_name : Observe
     * user : {"id":17,"name":"what-00@qq.com","img":null,"email":"what-00@qq.com"}
     * category : {"id":2,"type":1,"name":"Neo","long_name":"Neo","desc":"neo","img":"","cover_img":null,"url":null,"website":"https://neo.org/","unit":"NEO","ico_price":"-","token_holder":"neo","room_id":"40470541565955","is_hot":false,"is_top":true,"is_scroll":true,"search_rate":21,"click_rate":59,"type_name":"Trading","industry":"Blockchain"}
     */

    private int id;
    private int category_id;
    private int user_id;
    private boolean is_favorite;
    private boolean is_market_follow;
    private boolean is_favorite_dot;
    private boolean is_top;
    private String market_hige;
    private String market_lost;
    private String score;
    private int is_category_comment;
    private int category_comment_tag_id;
    private String category_comment;
    private String category_comment_at;
    private String category_comment_tag_name;
    private UserBean user;
    private CategoryBean category;
    private int user_click_comment_up_count;
    private int user_click_comment_down_count;
    private int user_click_comment_equal_count;
    private int comment_count;
    private List<UserClickCommentBean> user_click_comment;


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

    public List<UserClickCommentBean> getUser_click_comment() {
        return user_click_comment;
    }

    public void setUser_click_comment(List<UserClickCommentBean> user_click_comment) {
        this.user_click_comment = user_click_comment;
    }

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

    public String getCategory_comment_tag_name() {
        return category_comment_tag_name;
    }

    public void setCategory_comment_tag_name(String category_comment_tag_name) {
        this.category_comment_tag_name = category_comment_tag_name;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public CategoryBean getCategory() {
        return category;
    }

    public void setCategory(CategoryBean category) {
        this.category = category;
    }

    public static class UserBean implements Serializable{
        /**
         * id : 17
         * name : what-00@qq.com
         * img : null
         * email : what-00@qq.com
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

    public static class CategoryBean implements Serializable{
        /**
         * id : 2
         * type : 1
         * name : Neo
         * long_name : Neo
         * desc : neo
         * img :
         * cover_img : null
         * url : null
         * website : https://neo.org/
         * unit : NEO
         * ico_price : -
         * token_holder : neo
         * room_id : 40470541565955
         * is_hot : false
         * is_top : true
         * is_scroll : true
         * search_rate : 21
         * click_rate : 59
         * type_name : Trading
         * industry : Blockchain
         */

        private int id;
        private int type;
        private String name;
        private String long_name;
        private String desc;
        private String img;
        private String cover_img;
        private String url;
        private String website;
        private String unit;
        private String ico_price;
        private String token_holder;
        private String room_id;
        private boolean is_hot;
        private boolean is_top;
        private boolean is_scroll;
        private int search_rate;
        private int click_rate;
        private String type_name;
        private String industry;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getCover_img() {
            return cover_img;
        }

        public void setCover_img(String cover_img) {
            this.cover_img = cover_img;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getIco_price() {
            return ico_price;
        }

        public void setIco_price(String ico_price) {
            this.ico_price = ico_price;
        }

        public String getToken_holder() {
            return token_holder;
        }

        public void setToken_holder(String token_holder) {
            this.token_holder = token_holder;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
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
