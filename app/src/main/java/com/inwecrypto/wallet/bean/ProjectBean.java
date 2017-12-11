package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xiaoji06 on 2017/11/14 15:19
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectBean implements Serializable {


    /**
     * id : 6
     * type : 5
     * name : Bitcoin
     * long_name : null
     * en_name : null
     * desc : null
     * img : null
     * icon : null
     * style : null
     * url : null
     * sort : 0
     * is_top : 0
     * is_hot : 0
     * is_scroll : 0
     * seo_title : null
     * seo_keyworks : null
     * seo_desc : null
     * callback_fun : CategoryFun::getProjectDetail
     * score : null
     * grid_type : 4
     * is_cc : 0
     * is_comment : 0
     * is_save : 0
     * color : #000000
     * website :
     * project_markets : [{"id":1,"project_id":6,"name":"比特币","en_name":"BTC","long_name":null,"url":"ico/markets/kyber-network/btc","created_at":"2017-11-14 14:03:41","updated_at":"2017-11-14 14:03:44"},{"id":2,"project_id":6,"name":"以太坊","en_name":"ETH","long_name":null,"url":"ico/markets/kyber-network/eth","created_at":"2017-11-14 14:03:41","updated_at":"2017-11-14 14:03:44"}]
     * project_time_prices : [{"id":3,"project_id":6,"name":"以太坊","en_name":"ETH","long_name":null,"current_url":"ico/markets/kyber-network/eth","k_line_data_url":"ico/currencies/ethereum","created_at":null,"updated_at":null},{"id":2,"project_id":6,"name":"比特币","en_name":"BTC","long_name":null,"current_url":"ico/markets/kyber-network/btc","k_line_data_url":"ico/currencies/kyber-network","created_at":null,"updated_at":null},{"id":1,"project_id":6,"name":"美元","en_name":null,"long_name":null,"current_url":"ico/markets/kyber-network/btc","k_line_data_url":"ico/currencies/kyber-network","created_at":null,"updated_at":null}]
     * project_desc : [{"id":13,"title":"应用介绍"},{"id":12,"title":"团队介绍"},{"id":11,"title":"针对痛点"},{"id":10,"title":"项目简介"}]
     * project_detail : {"id":2,"project_id":16,"name":"KNC","ico_scale":"8.7亿KNC","total":"10亿KNC","accept":"BTC/ETH","start_at":"2017-11-15 13:02:41","end_at":"2017-11-18 13:02:42","crowdfund_periods":"无","target_quantity":"1400000.00","current_quantity":"4000000.00","is_online":0,"created_at":"2017-11-14 13:05:10","updated_at":"2017-11-14 13:05:13","risk_level":0}
     * ico_detail : [{"key":"token_sale","name":"TOKEN SALE:","value":"","desc":"14 NOV - 14 DEC"},{"key":"total_tokens","name":"Total Tokens:","value":"","desc":"10,000,000,000"},{"key":"fundraising_goal","name":"Fundraising Goal:","value":"","desc":"42,000,000 USD"},{"key":"token_price","name":"ICO Token Price:","value":"","desc":"1 NUG = 0.0160 USD"},{"key":"token_type","name":"Token type","value":"","desc":"ERC20"},{"key":"ticker","name":"Ticker:","value":"","desc":"NUG"},{"key":"accepts","name":"Accepts:","value":"","desc":"ETH, BTC, USD"},{"key":"token_issue","name":"Token Issue:","value":"","desc":"3 WEEKS AFTER ICO"},{"key":"personal_cap","name":"Min/Max Personal Cap:","value":"","desc":"NO / NO"},{"key":"kyc","name":"Know Your Customer (KYC):","value":"","desc":"NO"},{"key":"whitelist","name":"Whitelist:","value":"","desc":"NO"},{"key":"token_saie","name":"Available for Token Sale:","value":"","desc":"35%"}]
     * project_explorers : [{"name":"explorer.qtum.org","url":"https://explorer.qtum.org/","desc":null},{"name":"qtumexplorer.io","url":"https://qtumexplorer.io/","desc":null}]
     * project_wallets : [{"name":null,"url":"https://github.com/qtumproject/qtum/releases","desc":null}]
     * project_medias : [{"name":"facebook","url":"https://www.facebook.com/QtumOfficial/","desc":null},{"name":"twitter","url":"https://twitter.com/QtumOfficial","desc":null},{"name":"forum","url":"https://forum.qtum.org/","desc":null},{"name":"telegram","url":"https://t.me/joinchat/D5oBaw29NeOdpw6qqqf2lw","desc":null},{"name":"github","url":"https://github.com/qtumproject","desc":null}]
     */

    private int id;
    private int type;
    private String name;
    private String long_name;
    private String en_name;
    private String desc;
    private String img;
    private String icon;
    private String style;
    private String url;
    private int sort;
    private int is_top;
    private int is_hot;
    private int is_scroll;
    private String seo_title;
    private String seo_keyworks;
    private String seo_desc;
    private String callback_fun;
    private String score;
    private int grid_type;
    private int is_cc;
    private int is_comment;
    private int is_save;
    private String color;
    private int user_save;
    private String website;
    private boolean isOpen;
    private ProjectDetailBean project_detail;
    private ArrayList<ProjectMarketsBean> project_markets;
    private ArrayList<ProjectTimePricesBean> project_time_prices;
    private ArrayList<ProjectDescBean> project_desc;
    private ArrayList<IcoDetailBean> ico_detail;
    private ArrayList<ProjectExplorersBean> project_explorers;
    private ArrayList<ProjectWalletsBean> project_wallets;
    private ArrayList<ProjectMediasBean> project_medias;
    private String currentPrie;
    private String zhangfu;
    private String zuigao24;
    private String zuidi24;


    public String getZuigao24() {
        return zuigao24;
    }

    public void setZuigao24(String zuigao24) {
        this.zuigao24 = zuigao24;
    }

    public String getZuidi24() {
        return zuidi24;
    }

    public void setZuidi24(String zuidi24) {
        this.zuidi24 = zuidi24;
    }

    public String getCurrentPrie() {
        return currentPrie;
    }

    public void setCurrentPrie(String currentPrie) {
        this.currentPrie = currentPrie;
    }

    public String getZhangfu() {
        return zhangfu;
    }

    public void setZhangfu(String zhangfu) {
        this.zhangfu = zhangfu;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getUser_save() {
        return user_save;
    }

    public void setUser_save(int user_save) {
        this.user_save = user_save;
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

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getIs_top() {
        return is_top;
    }

    public void setIs_top(int is_top) {
        this.is_top = is_top;
    }

    public int getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(int is_hot) {
        this.is_hot = is_hot;
    }

    public int getIs_scroll() {
        return is_scroll;
    }

    public void setIs_scroll(int is_scroll) {
        this.is_scroll = is_scroll;
    }

    public String getSeo_title() {
        return seo_title;
    }

    public void setSeo_title(String seo_title) {
        this.seo_title = seo_title;
    }

    public String getSeo_keyworks() {
        return seo_keyworks;
    }

    public void setSeo_keyworks(String seo_keyworks) {
        this.seo_keyworks = seo_keyworks;
    }

    public String getSeo_desc() {
        return seo_desc;
    }

    public void setSeo_desc(String seo_desc) {
        this.seo_desc = seo_desc;
    }

    public String getCallback_fun() {
        return callback_fun;
    }

    public void setCallback_fun(String callback_fun) {
        this.callback_fun = callback_fun;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getGrid_type() {
        return grid_type;
    }

    public void setGrid_type(int grid_type) {
        this.grid_type = grid_type;
    }

    public int getIs_cc() {
        return is_cc;
    }

    public void setIs_cc(int is_cc) {
        this.is_cc = is_cc;
    }

    public int getIs_comment() {
        return is_comment;
    }

    public void setIs_comment(int is_comment) {
        this.is_comment = is_comment;
    }

    public int getIs_save() {
        return is_save;
    }

    public void setIs_save(int is_save) {
        this.is_save = is_save;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public ProjectDetailBean getProject_detail() {
        return project_detail;
    }

    public void setProject_detail(ProjectDetailBean project_detail) {
        this.project_detail = project_detail;
    }

    public ArrayList<ProjectMarketsBean> getProject_markets() {
        return project_markets;
    }

    public void setProject_markets(ArrayList<ProjectMarketsBean> project_markets) {
        this.project_markets = project_markets;
    }

    public ArrayList<ProjectTimePricesBean> getProject_time_prices() {
        return project_time_prices;
    }

    public void setProject_time_prices(ArrayList<ProjectTimePricesBean> project_time_prices) {
        this.project_time_prices = project_time_prices;
    }

    public ArrayList<ProjectDescBean> getProject_desc() {
        return project_desc;
    }

    public void setProject_desc(ArrayList<ProjectDescBean> project_desc) {
        this.project_desc = project_desc;
    }

    public ArrayList<IcoDetailBean> getIco_detail() {
        return ico_detail;
    }

    public void setIco_detail(ArrayList<IcoDetailBean> ico_detail) {
        this.ico_detail = ico_detail;
    }

    public ArrayList<ProjectExplorersBean> getProject_explorers() {
        return project_explorers;
    }

    public void setProject_explorers(ArrayList<ProjectExplorersBean> project_explorers) {
        this.project_explorers = project_explorers;
    }

    public ArrayList<ProjectWalletsBean> getProject_wallets() {
        return project_wallets;
    }

    public void setProject_wallets(ArrayList<ProjectWalletsBean> project_wallets) {
        this.project_wallets = project_wallets;
    }

    public ArrayList<ProjectMediasBean> getProject_medias() {
        return project_medias;
    }

    public void setProject_medias(ArrayList<ProjectMediasBean> project_medias) {
        this.project_medias = project_medias;
    }

    public static class ProjectDetailBean implements Serializable{
        /**
         * id : 2
         * project_id : 16
         * name : KNC
         * ico_scale : 8.7亿KNC
         * total : 10亿KNC
         * accept : BTC/ETH
         * start_at : 2017-11-15 13:02:41
         * end_at : 2017-11-18 13:02:42
         * crowdfund_periods : 无
         * target_quantity : 1400000.00
         * current_quantity : 4000000.00
         * is_online : 0
         * created_at : 2017-11-14 13:05:10
         * updated_at : 2017-11-14 13:05:13
         * risk_level : 0
         * risk_level_name : 极高
         * risk_level_color : #EF4346
         */

        private int id;
        private int project_id;
        private String name;
        private String ico_scale;
        private String total;
        private String accept;
        private String start_at;
        private String end_at;
        private String crowdfund_periods;
        private String target_quantity;
        private String current_quantity;
        private int is_online;
        private String created_at;
        private String updated_at;
        private int risk_level;
        private String risk_level_name;
        private String risk_level_color;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getProject_id() {
            return project_id;
        }

        public void setProject_id(int project_id) {
            this.project_id = project_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIco_scale() {
            return ico_scale;
        }

        public void setIco_scale(String ico_scale) {
            this.ico_scale = ico_scale;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getAccept() {
            return accept;
        }

        public void setAccept(String accept) {
            this.accept = accept;
        }

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

        public String getCrowdfund_periods() {
            return crowdfund_periods;
        }

        public void setCrowdfund_periods(String crowdfund_periods) {
            this.crowdfund_periods = crowdfund_periods;
        }

        public String getTarget_quantity() {
            return target_quantity;
        }

        public void setTarget_quantity(String target_quantity) {
            this.target_quantity = target_quantity;
        }

        public String getCurrent_quantity() {
            return current_quantity;
        }

        public void setCurrent_quantity(String current_quantity) {
            this.current_quantity = current_quantity;
        }

        public int getIs_online() {
            return is_online;
        }

        public void setIs_online(int is_online) {
            this.is_online = is_online;
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

        public int getRisk_level() {
            return risk_level;
        }

        public void setRisk_level(int risk_level) {
            this.risk_level = risk_level;
        }

        public String getRisk_level_name() {
            return risk_level_name;
        }

        public void setRisk_level_name(String risk_level_name) {
            this.risk_level_name = risk_level_name;
        }

        public String getRisk_level_color() {
            return risk_level_color;
        }

        public void setRisk_level_color(String risk_level_color) {
            this.risk_level_color = risk_level_color;
        }
    }

    public static class ProjectMarketsBean implements Serializable{
        /**
         * id : 1
         * project_id : 6
         * name : 比特币
         * en_name : BTC
         * long_name : null
         * url : ico/markets/kyber-network/btc
         * created_at : 2017-11-14 14:03:41
         * updated_at : 2017-11-14 14:03:44
         */

        private int id;
        private int project_id;
        private String name;
        private String en_name;
        private String long_name;
        private String url;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getProject_id() {
            return project_id;
        }

        public void setProject_id(int project_id) {
            this.project_id = project_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEn_name() {
            return en_name;
        }

        public void setEn_name(String en_name) {
            this.en_name = en_name;
        }

        public String getLong_name() {
            return long_name;
        }

        public void setLong_name(String long_name) {
            this.long_name = long_name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
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

    public static class ProjectTimePricesBean implements Serializable{
        /**
         * id : 3
         * project_id : 6
         * name : 以太坊
         * en_name : ETH
         * long_name : null
         * current_url : ico/markets/kyber-network/eth
         * k_line_data_url : ico/currencies/ethereum
         * created_at : null
         * updated_at : null
         */

        private int id;
        private int project_id;
        private String name;
        private String en_name;
        private String long_name;
        private String current_url;
        private String k_line_data_url;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getProject_id() {
            return project_id;
        }

        public void setProject_id(int project_id) {
            this.project_id = project_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEn_name() {
            return en_name;
        }

        public void setEn_name(String en_name) {
            this.en_name = en_name;
        }

        public String getLong_name() {
            return long_name;
        }

        public void setLong_name(String long_name) {
            this.long_name = long_name;
        }

        public String getCurrent_url() {
            return current_url;
        }

        public void setCurrent_url(String current_url) {
            this.current_url = current_url;
        }

        public String getK_line_data_url() {
            return k_line_data_url;
        }

        public void setK_line_data_url(String k_line_data_url) {
            this.k_line_data_url = k_line_data_url;
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

    public static class ProjectDescBean implements Serializable{
        /**
         * id : 13
         * title : 应用介绍
         */

        private int id;
        private String title;
        private boolean isShow;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isShow() {
            return isShow;
        }

        public void setShow(boolean show) {
            isShow = show;
        }
    }

    public static class IcoDetailBean implements Serializable{
        /**
         * key : token_sale
         * name : TOKEN SALE:
         * value :
         * desc : 14 NOV - 14 DEC
         */

        private String key;
        private String name;
        private String value;
        private String desc;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public static class ProjectExplorersBean implements Serializable{
        /**
         * name : explorer.qtum.org
         * url : https://explorer.qtum.org/
         * desc : null
         */

        private String name;
        private String url;
        private String desc;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public static class ProjectWalletsBean implements Serializable{
        /**
         * name : null
         * url : https://github.com/qtumproject/qtum/releases
         * desc : null
         */

        private String name;
        private String url;
        private String desc;
        private String img;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
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
    }

    public static class ProjectMediasBean implements Serializable{
        /**
         * name : facebook
         * url : https://www.facebook.com/QtumOfficial/
         * desc : null
         */

        private String name;
        private String url;
        private String desc;
        private String img;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
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
    }
}
