package com.inwecrypto.wallet.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/2/7 14:55
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectDetaileBean implements Serializable {

    /**
     * id : 2
     * type : 2
     * type_name : 众筹中
     * name : lightning
     * long_name : lightning
     * desc :
     * img :
     * cover_img : 项目封面图片 app
     * url :
     * website :
     * unit :
     * token_holder :
     * room_id : 0
     * is_hot : false
     * is_top : false
     * is_scroll : false
     * industry :
     * category_user : {"id":"1","category_id":"2","user_id":"5","is_favorite":true,"is_market_follow":true,"is_favorite_dot":true,"is_top":true,"market_hige":"100","market_lost":"80","score":"5","created_at":"2018-01-12 06:52:33","updated_at":"2018-01-14 20:59:57"}
     * last_article : {"id":"14","category_id":"10","title":"测试","desc":"图文测试 en23222","img":"","created_at":"2018-01-24 01:19:03","url":""}
     * ico : {"id":"qtum","name":"Qtum","symbol":"QTUM","rank":"17","price_usd":"40.8838","price_btc":"0.00359158","24h_volume_usd":"315202000.0","market_cap_usd":"3018651612.0","available_supply":"73834908.0","total_supply":"100334908.0","max_supply":"","percent_change_1h":"-0.24","percent_change_24h":"-1.39","percent_change_7d":"5.26","last_updated":"1516949055","price_cny":"5.68447926","24h_volume_cny":"10640223.6","market_cap_cny":"227379170.0"}
     */

    private String id;
    private int type;
    private String type_name;
    private String name;
    private String long_name;
    private String desc;
    private String img;
    private String cover_img;
    private String url;
    private String website;
    private String unit;
    private String token_holder;
    private String room_id;
    private boolean is_hot;
    private boolean is_top;
    private boolean is_scroll;
    private String industry;
    private CategoryUserBean category_user;
    private LastArticleBean last_article;
    private IcoBean ico;
    private boolean isCommonProject;
    private CommonProjectBean commonProjectBean;
    private String price;
    private String charge;
    private String time;
    private String message;
    private boolean hasMarket=true;

    public boolean isHasMarket() {
        return hasMarket;
    }

    public void setHasMarket(boolean hasMarket) {
        this.hasMarket = hasMarket;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public boolean isCommonProject() {
        return isCommonProject;
    }

    public void setCommonProject(boolean commonProject) {
        isCommonProject = commonProject;
    }

    public CommonProjectBean getCommonProjectBean() {
        return commonProjectBean;
    }

    public void setCommonProjectBean(CommonProjectBean commonProjectBean) {
        this.commonProjectBean = commonProjectBean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public CategoryUserBean getCategory_user() {
        return category_user;
    }

    public void setCategory_user(CategoryUserBean category_user) {
        this.category_user = category_user;
    }

    public LastArticleBean getLast_article() {
        return last_article;
    }

    public void setLast_article(LastArticleBean last_article) {
        this.last_article = last_article;
    }

    public IcoBean getIco() {
        return ico;
    }

    public void setIco(IcoBean ico) {
        this.ico = ico;
    }

    public static class CategoryUserBean implements Serializable{
        /**
         * id : 1
         * category_id : 2
         * user_id : 5
         * is_favorite : true
         * is_market_follow : true
         * is_favorite_dot : true
         * is_top : true
         * market_hige : 100
         * market_lost : 80
         * score : 5
         * created_at : 2018-01-12 06:52:33
         * updated_at : 2018-01-14 20:59:57
         */

        private String id;
        private String category_id;
        private String user_id;
        private boolean is_favorite;
        private boolean is_market_follow;
        private boolean is_favorite_dot;
        private boolean is_top;
        private String market_hige;
        private String market_lost;
        private String score;
        private String created_at;
        private String updated_at;

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

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
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

    public static class LastArticleBean implements Serializable{
        /**
         * id : 14
         * category_id : 10
         * title : 测试
         * desc : 图文测试 en23222
         * img :
         * created_at : 2018-01-24 01:19:03
         * url :
         */

        private String id;
        private String category_id;
        private String title;
        private String desc;
        private String img;
        private String created_at;
        private String url;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class IcoBean implements Serializable{
        /**
         * id : qtum
         * name : Qtum
         * symbol : QTUM
         * rank : 17
         * price_usd : 40.8838
         * price_btc : 0.00359158
         * 24h_volume_usd : 315202000.0
         * market_cap_usd : 3018651612.0
         * available_supply : 73834908.0
         * total_supply : 100334908.0
         * max_supply :
         * percent_change_1h : -0.24
         * percent_change_24h : -1.39
         * percent_change_7d : 5.26
         * last_updated : 1516949055
         * price_cny : 5.68447926
         * 24h_volume_cny : 10640223.6
         * market_cap_cny : 227379170.0
         */

        private String id;
        private String name;
        private String symbol;
        private String rank;
        private String price_usd;
        private String price_btc;
        @SerializedName("24h_volume_usd")
        private String _$24h_volume_usd;
        private String market_cap_usd;
        private String available_supply;
        private String total_supply;
        private String max_supply;
        private String percent_change_1h;
        private String percent_change_24h;
        private String percent_change_7d;
        private String last_updated;
        private String price_cny;
        @SerializedName("24h_volume_cny")
        private String _$24h_volume_cny;
        private String market_cap_cny;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getPrice_usd() {
            return price_usd;
        }

        public void setPrice_usd(String price_usd) {
            this.price_usd = price_usd;
        }

        public String getPrice_btc() {
            return price_btc;
        }

        public void setPrice_btc(String price_btc) {
            this.price_btc = price_btc;
        }

        public String get_$24h_volume_usd() {
            return _$24h_volume_usd;
        }

        public void set_$24h_volume_usd(String _$24h_volume_usd) {
            this._$24h_volume_usd = _$24h_volume_usd;
        }

        public String getMarket_cap_usd() {
            return market_cap_usd;
        }

        public void setMarket_cap_usd(String market_cap_usd) {
            this.market_cap_usd = market_cap_usd;
        }

        public String getAvailable_supply() {
            return available_supply;
        }

        public void setAvailable_supply(String available_supply) {
            this.available_supply = available_supply;
        }

        public String getTotal_supply() {
            return total_supply;
        }

        public void setTotal_supply(String total_supply) {
            this.total_supply = total_supply;
        }

        public String getMax_supply() {
            return max_supply;
        }

        public void setMax_supply(String max_supply) {
            this.max_supply = max_supply;
        }

        public String getPercent_change_1h() {
            return percent_change_1h;
        }

        public void setPercent_change_1h(String percent_change_1h) {
            this.percent_change_1h = percent_change_1h;
        }

        public String getPercent_change_24h() {
            return percent_change_24h;
        }

        public void setPercent_change_24h(String percent_change_24h) {
            this.percent_change_24h = percent_change_24h;
        }

        public String getPercent_change_7d() {
            return percent_change_7d;
        }

        public void setPercent_change_7d(String percent_change_7d) {
            this.percent_change_7d = percent_change_7d;
        }

        public String getLast_updated() {
            return last_updated;
        }

        public void setLast_updated(String last_updated) {
            this.last_updated = last_updated;
        }

        public String getPrice_cny() {
            return price_cny;
        }

        public void setPrice_cny(String price_cny) {
            this.price_cny = price_cny;
        }

        public String get_$24h_volume_cny() {
            return _$24h_volume_cny;
        }

        public void set_$24h_volume_cny(String _$24h_volume_cny) {
            this._$24h_volume_cny = _$24h_volume_cny;
        }

        public String getMarket_cap_cny() {
            return market_cap_cny;
        }

        public void setMarket_cap_cny(String market_cap_cny) {
            this.market_cap_cny = market_cap_cny;
        }
    }
}
