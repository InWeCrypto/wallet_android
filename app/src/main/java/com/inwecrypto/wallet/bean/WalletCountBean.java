package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/16.
 * 功能描述：
 * 版本：@version
 */

public class WalletCountBean implements Serializable {

    /**
     * id : 21
     * user_id : 1
     * category_id : 1
     * name : 观察钱包测试
     * address : 0x2a6aa2121e8e2158a4087969675099204ec5da98
     * created_at : 2017-08-15 23:46:41
     * updated_at : 2017-08-15 23:46:41
     * deleted_at : null
     * balance : 0x28456757671e2fe0
     * category : {"id":1,"name":"ETH","cap":{"id":12831458,"asset_id":"omisego","name":"OmiseGo","symbol":"OMG","rank":"14","price_usd":"6.49145","price_btc":"0.00152059","volume_usd_24h":"82698400.0","market_cap_usd":"638187588.0","available_supply":"98312024.0","total_supply":"140245398.0","percent_change_1h":"-0.37","percent_change_24h":"-8.19","percent_change_7d":"116.54","last_updated":"1502738361","price_cny":"43.28888347","volume_cny_24h":"551482550.24","market_cap_cny":"4255817751.0"}}
     */

    private int id;
    private int user_id;
    private int category_id;
    private String name;
    private String address;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private String balance;
    private CategoryBean category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public CategoryBean getCategory() {
        return category;
    }

    public void setCategory(CategoryBean category) {
        this.category = category;
    }

    public static class CategoryBean implements Serializable{
        /**
         * id : 1
         * name : ETH
         * cap : {"id":12831458,"asset_id":"omisego","name":"OmiseGo","symbol":"OMG","rank":"14","price_usd":"6.49145","price_btc":"0.00152059","volume_usd_24h":"82698400.0","market_cap_usd":"638187588.0","available_supply":"98312024.0","total_supply":"140245398.0","percent_change_1h":"-0.37","percent_change_24h":"-8.19","percent_change_7d":"116.54","last_updated":"1502738361","price_cny":"43.28888347","volume_cny_24h":"551482550.24","market_cap_cny":"4255817751.0"}
         */

        private int id;
        private String name;
        private CapBean cap;

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

        public CapBean getCap() {
            return cap;
        }

        public void setCap(CapBean cap) {
            this.cap = cap;
        }

        public static class CapBean implements Serializable{
            /**
             * id : 12831458
             * asset_id : omisego
             * name : OmiseGo
             * symbol : OMG
             * rank : 14
             * price_usd : 6.49145
             * price_btc : 0.00152059
             * volume_usd_24h : 82698400.0
             * market_cap_usd : 638187588.0
             * available_supply : 98312024.0
             * total_supply : 140245398.0
             * percent_change_1h : -0.37
             * percent_change_24h : -8.19
             * percent_change_7d : 116.54
             * last_updated : 1502738361
             * price_cny : 43.28888347
             * volume_cny_24h : 551482550.24
             * market_cap_cny : 4255817751.0
             */

            private String id;
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

            public String getId() {
                return id;
            }

            public void setId(String id) {
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
    }
}
