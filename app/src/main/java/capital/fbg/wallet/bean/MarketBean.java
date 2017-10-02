package capital.fbg.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/9.
 * 功能描述：
 * 版本：@version
 */

public class MarketBean implements Serializable {

    /**
     * id : 1
     * name : OMG
     * flag : OMG
     * token : OMG
     * url : https://OMG
     * created_at : 2017-08-07 17:17:12
     * updated_at : 2017-08-14 17:45:57
     * relationCap : {"id":12184841,"asset_id":"omisego","name":"OmiseGo","symbol":"OMG","rank":"14","price_usd":"6.39778","price_btc":"0.00151983","volume_usd_24h":"113586000.0","market_cap_usd":"628978701.0","available_supply":"98312024.0","total_supply":"140245398.0","percent_change_1h":"-4.81","percent_change_24h":"3.82","percent_change_7d":"114.61","last_updated":"1502705660","price_cny":"42.644722479","volume_cny_24h":"757113162.3","market_cap_cny":"4192488980.0"}
     * relationCapMin : {"id":2041598,"asset_id":"omisego","name":"OmiseGo","symbol":"OMG","rank":"14","price_usd_first":"6.397780","price_usd_last":"6.397780","price_usd_low":"6.397780","price_usd_high":"6.397780","price_btc_first":"0.001520","price_btc_last":"0.001520","price_btc_low":"0.001520","price_btc_high":"0.001520","price_cny_first":"42.644722","price_cny_last":"42.644722","price_cny_low":"42.644722","price_cny_high":"42.644722","last_updated":"1502705660","timestamp":1502705731}
     */

    private int id;
    private String name;
    private String flag;
    private String token;
    private String url;
    private String created_at;
    private String updated_at;
    private RelationCapBean relationCap;
    private RelationCapMinBean relationCapMin;
    private boolean select;
    private int relation_user_count;

    public int getRelation_user_count() {
        return relation_user_count;
    }

    public void setRelation_user_count(int relation_user_count) {
        this.relation_user_count = relation_user_count;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public RelationCapBean getRelationCap() {
        return relationCap;
    }

    public void setRelationCap(RelationCapBean relationCap) {
        this.relationCap = relationCap;
    }

    public RelationCapMinBean getRelationCapMin() {
        return relationCapMin;
    }

    public void setRelationCapMin(RelationCapMinBean relationCapMin) {
        this.relationCapMin = relationCapMin;
    }

    public static class RelationCapBean implements Serializable{
        /**
         * id : 12184841
         * asset_id : omisego
         * name : OmiseGo
         * symbol : OMG
         * rank : 14
         * price_usd : 6.39778
         * price_btc : 0.00151983
         * volume_usd_24h : 113586000.0
         * market_cap_usd : 628978701.0
         * available_supply : 98312024.0
         * total_supply : 140245398.0
         * percent_change_1h : -4.81
         * percent_change_24h : 3.82
         * percent_change_7d : 114.61
         * last_updated : 1502705660
         * price_cny : 42.644722479
         * volume_cny_24h : 757113162.3
         * market_cap_cny : 4192488980.0
         */

        private int id;
        private String asset_id;
        private String name;
        private String symbol;
        private String rank;
        private String price_usd;
        private String price_btc;
        private String volume_usd_24h;
        private String market_cap_usd;
        private String available_supply;
        private String total_supply;
        private String percent_change_1h;
        private String percent_change_24h;
        private String percent_change_7d;
        private String last_updated;
        private String price_cny;
        private String volume_cny_24h;
        private String market_cap_cny;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAsset_id() {
            return asset_id;
        }

        public void setAsset_id(String asset_id) {
            this.asset_id = asset_id;
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

        public String getVolume_usd_24h() {
            return volume_usd_24h;
        }

        public void setVolume_usd_24h(String volume_usd_24h) {
            this.volume_usd_24h = volume_usd_24h;
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

        public String getVolume_cny_24h() {
            return volume_cny_24h;
        }

        public void setVolume_cny_24h(String volume_cny_24h) {
            this.volume_cny_24h = volume_cny_24h;
        }

        public String getMarket_cap_cny() {
            return market_cap_cny;
        }

        public void setMarket_cap_cny(String market_cap_cny) {
            this.market_cap_cny = market_cap_cny;
        }
    }

    public static class RelationCapMinBean implements Serializable{
        /**
         * id : 2041598
         * asset_id : omisego
         * name : OmiseGo
         * symbol : OMG
         * rank : 14
         * price_usd_first : 6.397780
         * price_usd_last : 6.397780
         * price_usd_low : 6.397780
         * price_usd_high : 6.397780
         * price_btc_first : 0.001520
         * price_btc_last : 0.001520
         * price_btc_low : 0.001520
         * price_btc_high : 0.001520
         * price_cny_first : 42.644722
         * price_cny_last : 42.644722
         * price_cny_low : 42.644722
         * price_cny_high : 42.644722
         * last_updated : 1502705660
         * timestamp : 1502705731
         */

        private int id;
        private String asset_id;
        private String name;
        private String symbol;
        private String rank;
        private String price_usd_first;
        private String price_usd_last;
        private String price_usd_low;
        private String price_usd_high;
        private String price_btc_first;
        private String price_btc_last;
        private String price_btc_low;
        private String price_btc_high;
        private String price_cny_first;
        private String price_cny_last;
        private String price_cny_low;
        private String price_cny_high;
        private String last_updated;
        private int timestamp;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAsset_id() {
            return asset_id;
        }

        public void setAsset_id(String asset_id) {
            this.asset_id = asset_id;
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

        public String getPrice_usd_first() {
            return price_usd_first;
        }

        public void setPrice_usd_first(String price_usd_first) {
            this.price_usd_first = price_usd_first;
        }

        public String getPrice_usd_last() {
            return price_usd_last;
        }

        public void setPrice_usd_last(String price_usd_last) {
            this.price_usd_last = price_usd_last;
        }

        public String getPrice_usd_low() {
            return price_usd_low;
        }

        public void setPrice_usd_low(String price_usd_low) {
            this.price_usd_low = price_usd_low;
        }

        public String getPrice_usd_high() {
            return price_usd_high;
        }

        public void setPrice_usd_high(String price_usd_high) {
            this.price_usd_high = price_usd_high;
        }

        public String getPrice_btc_first() {
            return price_btc_first;
        }

        public void setPrice_btc_first(String price_btc_first) {
            this.price_btc_first = price_btc_first;
        }

        public String getPrice_btc_last() {
            return price_btc_last;
        }

        public void setPrice_btc_last(String price_btc_last) {
            this.price_btc_last = price_btc_last;
        }

        public String getPrice_btc_low() {
            return price_btc_low;
        }

        public void setPrice_btc_low(String price_btc_low) {
            this.price_btc_low = price_btc_low;
        }

        public String getPrice_btc_high() {
            return price_btc_high;
        }

        public void setPrice_btc_high(String price_btc_high) {
            this.price_btc_high = price_btc_high;
        }

        public String getPrice_cny_first() {
            return price_cny_first;
        }

        public void setPrice_cny_first(String price_cny_first) {
            this.price_cny_first = price_cny_first;
        }

        public String getPrice_cny_last() {
            return price_cny_last;
        }

        public void setPrice_cny_last(String price_cny_last) {
            this.price_cny_last = price_cny_last;
        }

        public String getPrice_cny_low() {
            return price_cny_low;
        }

        public void setPrice_cny_low(String price_cny_low) {
            this.price_cny_low = price_cny_low;
        }

        public String getPrice_cny_high() {
            return price_cny_high;
        }

        public void setPrice_cny_high(String price_cny_high) {
            this.price_cny_high = price_cny_high;
        }

        public String getLast_updated() {
            return last_updated;
        }

        public void setLast_updated(String last_updated) {
            this.last_updated = last_updated;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }
    }
}
