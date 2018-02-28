package com.inwecrypto.wallet.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/11 16:19
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class TradingProjectDetaileBean implements Serializable {

    /**
     * id : 7
     * type : 1
     * type_name : 上线中
     * name : Qtum
     * long_name : Qtum
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
     * category_user : {"id":1,"category_id":2,"user_id":5,"is_favorite":true,"is_market_follow":true,"is_favorite_dot":true,"is_top":true,"market_hige":"100","market_lost":"80","score":"5"}
     * industry :
     * category_explorer : [{"name":"qtumexplorer.io","desc":"","url":"https://qtumexplorer.io/"}]
     * category_wallet : [{"name":"","desc":"","url":"https://github.com/qtumproject/qtum/releases"}]
     * category_structure : [{"percentage":40,"color_name":"粉色","color_value":"#EC87BF","desc":"5亿用于公开发行","lang":"zh"}]
     * category_desc : {"start_at":"2018-01-14 06:00:00","end_at":"2018-01-16 06:00:00","content":"","lang":"zh"}
     * category_media : [{"img":"http://whalewallet.oss-cn-hongkong.aliyuncs.com/media_icon/facebook.png","name":"facebook","desc":"","url":"https://www.facebook.com/QtumOfficial/","qr_img":""}]
     * category_score : {"value":4.75,"sort":1}
     * category_presentation : {"id":10,"category_id":7,"content":"QTUM 项目介绍 en"}
     * last_article : {"id":14,"category_id":10,"title":"测试","desc":"图文测试 en23222","img":"","created_at":"2018-01-24 01:19:03","url":""}
     */

    private int id;
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
    private String ico_price;
    private String token_holder;
    private String room_id;
    private boolean is_hot;
    private boolean is_top;
    private boolean is_scroll;
    private CategoryUserBean category_user;
    private String industry;
    private CategoryDescBean category_desc;
    private CategoryScoreBean category_score;
    private CategoryPresentationBean category_presentation;
    private LastArticleBean last_article;
    private List<CategoryExplorerBean> category_explorer;
    private List<CategoryWalletBean> category_wallet;
    private List<CategoryStructureBean> category_structure;
    private List<CategoryMediaBean> category_media;
    private IcoBean ico;

    public String getIco_price() {
        return ico_price;
    }

    public void setIco_price(String ico_price) {
        this.ico_price = ico_price;
    }

    public IcoBean getIco() {
        return ico;
    }

    public void setIco(IcoBean ico) {
        this.ico = ico;
    }

    public static class IcoBean  implements Serializable{

        /**
         * id : ethereum
         * name : Ethereum
         * symbol : ETH
         * rank : 2
         * price_usd : 794.058
         * price_btc : 0.100329
         * 24h_volume_usd : 2551760000.0
         * market_cap_usd : 77456875905.0
         * available_supply : 97545615.0
         * total_supply : 97545615.0
         * max_supply : null
         * percent_change_1h : 0.33
         * percent_change_24h : -9.93
         * percent_change_7d : -14.26
         * last_updated : 1518341652
         * price_cny : 4993.5131388
         * 24h_volume_cny : 16046997936.0
         * market_cap_cny : 487095309818
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

    public CategoryUserBean getCategory_user() {
        return category_user;
    }

    public void setCategory_user(CategoryUserBean category_user) {
        this.category_user = category_user;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public CategoryDescBean getCategory_desc() {
        return category_desc;
    }

    public void setCategory_desc(CategoryDescBean category_desc) {
        this.category_desc = category_desc;
    }

    public CategoryScoreBean getCategory_score() {
        return category_score;
    }

    public void setCategory_score(CategoryScoreBean category_score) {
        this.category_score = category_score;
    }

    public CategoryPresentationBean getCategory_presentation() {
        return category_presentation;
    }

    public void setCategory_presentation(CategoryPresentationBean category_presentation) {
        this.category_presentation = category_presentation;
    }

    public LastArticleBean getLast_article() {
        return last_article;
    }

    public void setLast_article(LastArticleBean last_article) {
        this.last_article = last_article;
    }

    public List<CategoryExplorerBean> getCategory_explorer() {
        return category_explorer;
    }

    public void setCategory_explorer(List<CategoryExplorerBean> category_explorer) {
        this.category_explorer = category_explorer;
    }

    public List<CategoryWalletBean> getCategory_wallet() {
        return category_wallet;
    }

    public void setCategory_wallet(List<CategoryWalletBean> category_wallet) {
        this.category_wallet = category_wallet;
    }

    public List<CategoryStructureBean> getCategory_structure() {
        return category_structure;
    }

    public void setCategory_structure(List<CategoryStructureBean> category_structure) {
        this.category_structure = category_structure;
    }

    public List<CategoryMediaBean> getCategory_media() {
        return category_media;
    }

    public void setCategory_media(List<CategoryMediaBean> category_media) {
        this.category_media = category_media;
    }

    public static class CategoryUserBean  implements Serializable{
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
    }

    public static class CategoryDescBean  implements Serializable{
        /**
         * start_at : 2018-01-14 06:00:00
         * end_at : 2018-01-16 06:00:00
         * content :
         * lang : zh
         */

        private String start_at;
        private String end_at;
        private String content;
        private String lang;

        public String getStart_at() {
            return start_at;
        }

        public void setStart_at(String start_at) {
            this.start_at = start_at;
        }

        public String getEnd_at() {
            return end_at;
        }

        public void setEnd_at(String end_at) {
            this.end_at = end_at;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }
    }

    public static class CategoryScoreBean  implements Serializable{
        /**
         * value : 4.75
         * sort : 1
         */

        private double value;
        private int sort;

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }
    }

    public static class CategoryPresentationBean  implements Serializable{
        /**
         * id : 10
         * category_id : 7
         * content : QTUM 项目介绍 en
         */

        private int id;
        private int category_id;
        private String content;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class LastArticleBean  implements Serializable{
        /**
         * id : 14
         * category_id : 10
         * title : 测试
         * desc : 图文测试 en23222
         * img :
         * created_at : 2018-01-24 01:19:03
         * url :
         */

        private int id;
        private int category_id;
        private String title;
        private String desc;
        private String img;
        private String created_at;
        private String url;

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

    public static class CategoryExplorerBean  implements Serializable{
        /**
         * name : qtumexplorer.io
         * desc :
         * url : https://qtumexplorer.io/
         */

        private String name;
        private String desc;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
    }

    public static class CategoryWalletBean  implements Serializable{
        /**
         * name :
         * desc :
         * url : https://github.com/qtumproject/qtum/releases
         */

        private String name;
        private String desc;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
    }

    public static class CategoryStructureBean  implements Serializable{
        /**
         * percentage : 40
         * color_name : 粉色
         * color_value : #EC87BF
         * desc : 5亿用于公开发行
         * lang : zh
         */

        private int percentage;
        private String color_name;
        private String color_value;
        private String desc;
        private String lang;

        public int getPercentage() {
            return percentage;
        }

        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }

        public String getColor_name() {
            return color_name;
        }

        public void setColor_name(String color_name) {
            this.color_name = color_name;
        }

        public String getColor_value() {
            return color_value;
        }

        public void setColor_value(String color_value) {
            this.color_value = color_value;
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
    }

    public static class CategoryMediaBean  implements Serializable{
        /**
         * img : http://whalewallet.oss-cn-hongkong.aliyuncs.com/media_icon/facebook.png
         * name : facebook
         * desc :
         * url : https://www.facebook.com/QtumOfficial/
         * qr_img :
         */

        private String img;
        private String name;
        private String desc;
        private String url;
        private String qr_img;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getQr_img() {
            return qr_img;
        }

        public void setQr_img(String qr_img) {
            this.qr_img = qr_img;
        }
    }
}
