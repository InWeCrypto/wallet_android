package com.inwecrypto.wallet.bean;

import com.google.gson.annotations.SerializedName;

import org.w3c.dom.ProcessingInstruction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 * 功能描述：
 * 版本：@version
 */

public class TokenBean implements Serializable{

    /**
     * record : {"id":22,"user_id":1,"category_id":1,"name":"代币","address":"0x00c700301974242247612401af54e67ee09add2d","created_at":"2017-08-16 00:21:46","updated_at":"2017-08-16 00:21:46","deleted_at":null,"gnt":[{"id":6,"user_id":1,"gnt_category_id":1,"name":"SNT","created_at":"2017-08-16 00:32:24","updated_at":"2017-08-16 00:32:24","wallet_id":22,"balance":"0x0000000000000000000000000000000000000000000000000000000000000000","gnt_category":{"id":1,"category_id":1,"name":"SNT","created_at":"2017-07-30 23:01:27","updated_at":"2017-08-15 18:55:59","icon":"http://cryptobox.oss-cn-shenzhen.aliyuncs.com/7071bdd3-1366-3866-932b-6893a2e03a07.png","address":"0xd9d700125b05f26df706f7190fa3be40c29af2fa","gas":"11","cap":{"id":12831458,"asset_id":"omisego","name":"OmiseGo","symbol":"OMG","rank":"14","price_usd":"6.49145","price_btc":"0.00152059","volume_usd_24h":"82698400.0","market_cap_usd":"638187588.0","available_supply":"98312024.0","total_supply":"140245398.0","percent_change_1h":"-0.37","percent_change_24h":"-8.19","percent_change_7d":"116.54","last_updated":"1502738361","price_cny":"43.28888347","volume_cny_24h":"551482550.24","market_cap_cny":"4255817751.0"}}},{"id":7,"user_id":1,"gnt_category_id":2,"name":"OMG","created_at":"2017-08-16 00:32:24","updated_at":"2017-08-16 00:32:24","wallet_id":22,"balance":"0x0000000000000000000000000000000000000000000000000000000000000000","gnt_category":{"id":2,"category_id":1,"name":"OMG","created_at":"2017-08-15 18:56:29","updated_at":"2017-08-15 19:00:11","icon":"http://cryptobox.oss-cn-shenzhen.aliyuncs.com/94a4ee25-a651-3726-88ce-e3a8eebbcb86.png","address":"0xd9d700125b05f26df706f7190fa3be40c29af2fa","gas":"11","cap":{"id":12831458,"asset_id":"omisego","name":"OmiseGo","symbol":"OMG","rank":"14","price_usd":"6.49145","price_btc":"0.00152059","volume_usd_24h":"82698400.0","market_cap_usd":"638187588.0","available_supply":"98312024.0","total_supply":"140245398.0","percent_change_1h":"-0.37","percent_change_24h":"-8.19","percent_change_7d":"116.54","last_updated":"1502738361","price_cny":"43.28888347","volume_cny_24h":"551482550.24","market_cap_cny":"4255817751.0"}}}]}
     * list : [{"id":6,"user_id":1,"gnt_category_id":1,"name":"SNT","created_at":"2017-08-16 00:32:24","updated_at":"2017-08-16 00:32:24","wallet_id":22,"balance":"0x0000000000000000000000000000000000000000000000000000000000000000","gnt_category":{"id":1,"category_id":1,"name":"SNT","created_at":"2017-07-30 23:01:27","updated_at":"2017-08-15 18:55:59","icon":"http://cryptobox.oss-cn-shenzhen.aliyuncs.com/7071bdd3-1366-3866-932b-6893a2e03a07.png","address":"0xd9d700125b05f26df706f7190fa3be40c29af2fa","gas":"11","cap":{"id":12831458,"asset_id":"omisego","name":"OmiseGo","symbol":"OMG","rank":"14","price_usd":"6.49145","price_btc":"0.00152059","volume_usd_24h":"82698400.0","market_cap_usd":"638187588.0","available_supply":"98312024.0","total_supply":"140245398.0","percent_change_1h":"-0.37","percent_change_24h":"-8.19","percent_change_7d":"116.54","last_updated":"1502738361","price_cny":"43.28888347","volume_cny_24h":"551482550.24","market_cap_cny":"4255817751.0"}}}]
     */

    private RecordBean record;
    private List<ListBean> list;

    public RecordBean getRecord() {
        return record;
    }

    public void setRecord(RecordBean record) {
        this.record = record;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class RecordBean implements Serializable{
        /**
         * id : 22
         * user_id : 1
         * category_id : 1
         * name : 代币
         * address : 0x00c700301974242247612401af54e67ee09add2d
         * created_at : 2017-08-16 00:21:46
         * updated_at : 2017-08-16 00:21:46
         * deleted_at : null
         * gnt : [{"id":6,"user_id":1,"gnt_category_id":1,"name":"SNT","created_at":"2017-08-16 00:32:24","updated_at":"2017-08-16 00:32:24","wallet_id":22,"balance":"0x0000000000000000000000000000000000000000000000000000000000000000","gnt_category":{"id":1,"category_id":1,"name":"SNT","created_at":"2017-07-30 23:01:27","updated_at":"2017-08-15 18:55:59","icon":"http://cryptobox.oss-cn-shenzhen.aliyuncs.com/7071bdd3-1366-3866-932b-6893a2e03a07.png","address":"0xd9d700125b05f26df706f7190fa3be40c29af2fa","gas":"11","cap":{"id":12831458,"asset_id":"omisego","name":"OmiseGo","symbol":"OMG","rank":"14","price_usd":"6.49145","price_btc":"0.00152059","volume_usd_24h":"82698400.0","market_cap_usd":"638187588.0","available_supply":"98312024.0","total_supply":"140245398.0","percent_change_1h":"-0.37","percent_change_24h":"-8.19","percent_change_7d":"116.54","last_updated":"1502738361","price_cny":"43.28888347","volume_cny_24h":"551482550.24","market_cap_cny":"4255817751.0"}}},{"id":7,"user_id":1,"gnt_category_id":2,"name":"OMG","created_at":"2017-08-16 00:32:24","updated_at":"2017-08-16 00:32:24","wallet_id":22,"balance":"0x0000000000000000000000000000000000000000000000000000000000000000","gnt_category":{"id":2,"category_id":1,"name":"OMG","created_at":"2017-08-15 18:56:29","updated_at":"2017-08-15 19:00:11","icon":"http://cryptobox.oss-cn-shenzhen.aliyuncs.com/94a4ee25-a651-3726-88ce-e3a8eebbcb86.png","address":"0xd9d700125b05f26df706f7190fa3be40c29af2fa","gas":"11","cap":{"id":12831458,"asset_id":"omisego","name":"OmiseGo","symbol":"OMG","rank":"14","price_usd":"6.49145","price_btc":"0.00152059","volume_usd_24h":"82698400.0","market_cap_usd":"638187588.0","available_supply":"98312024.0","total_supply":"140245398.0","percent_change_1h":"-0.37","percent_change_24h":"-8.19","percent_change_7d":"116.54","last_updated":"1502738361","price_cny":"43.28888347","volume_cny_24h":"551482550.24","market_cap_cny":"4255817751.0"}}}]
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
        private CapBean cap;
        private List<GntBean> gnt;
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

        public List<GntBean> getGnt() {
            return gnt;
        }

        public void setGnt(List<GntBean> gnt) {
            this.gnt = gnt;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public CapBean getCap() {
            return cap;
        }

        public void setCap(CapBean cap) {
            this.cap = cap;
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
             */

            private int id;
            private String name;

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
        }

        public static class CapBean implements Serializable{

            /**
             * id : 160355199
             * asset_id : neo
             * name : NEO
             * symbol : NEO
             * rank : 11
             * price_usd : 39.5524
             * price_btc : 0.00401056
             * volume_usd_24h : 83149400.0
             * market_cap_usd : 2570906000.0
             * available_supply : 65000000.0
             * total_supply : 100000000.0
             * percent_change_1h : -0.21
             * percent_change_24h : -2.78
             * percent_change_7d : 10.7
             * last_updated : 1511850553
             * price_cny : 261.2040496
             * volume_cny_24h : 549118637.6
             * market_cap_cny : 16978263224.0
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

        public static class GntBean implements Serializable{
            /**
             * id : 6
             * user_id : 1
             * gnt_category_id : 1
             * name : SNT
             * created_at : 2017-08-16 00:32:24
             * updated_at : 2017-08-16 00:32:24
             * wallet_id : 22
             * balance : 0x0000000000000000000000000000000000000000000000000000000000000000
             * gnt_category : {"id":1,"category_id":1,"name":"SNT","created_at":"2017-07-30 23:01:27","updated_at":"2017-08-15 18:55:59","icon":"http://cryptobox.oss-cn-shenzhen.aliyuncs.com/7071bdd3-1366-3866-932b-6893a2e03a07.png","address":"0xd9d700125b05f26df706f7190fa3be40c29af2fa","gas":"11","cap":{"id":12831458,"asset_id":"omisego","name":"OmiseGo","symbol":"OMG","rank":"14","price_usd":"6.49145","price_btc":"0.00152059","volume_usd_24h":"82698400.0","market_cap_usd":"638187588.0","available_supply":"98312024.0","total_supply":"140245398.0","percent_change_1h":"-0.37","percent_change_24h":"-8.19","percent_change_7d":"116.54","last_updated":"1502738361","price_cny":"43.28888347","volume_cny_24h":"551482550.24","market_cap_cny":"4255817751.0"}}
             */


            private String id;
            private int user_id;
            private int gnt_category_id;
            private String name;
            private String created_at;
            private String updated_at;
            private int wallet_id;
            private String balance;
            private String decimals;
            private String unavailable;
            private String available;
            private GntCategoryBean gnt_category;
            private CapBean cap;

            public String getDecimals() {
                return decimals;
            }

            public void setDecimals(String decimals) {
                this.decimals = decimals;
            }

            public String getUnavailable() {
                return unavailable;
            }

            public void setUnavailable(String unavailable) {
                this.unavailable = unavailable;
            }

            public String getAvailable() {
                return available;
            }

            public void setAvailable(String available) {
                this.available = available;
            }

            public CapBean getCap() {
                return cap;
            }

            public void setCap(CapBean cap) {
                this.cap = cap;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getGnt_category_id() {
                return gnt_category_id;
            }

            public void setGnt_category_id(int gnt_category_id) {
                this.gnt_category_id = gnt_category_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public int getWallet_id() {
                return wallet_id;
            }

            public void setWallet_id(int wallet_id) {
                this.wallet_id = wallet_id;
            }

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public GntCategoryBean getGnt_category() {
                return gnt_category;
            }

            public void setGnt_category(GntCategoryBean gnt_category) {
                this.gnt_category = gnt_category;
            }

            public static class CapBean implements Serializable{

                /**
                 * id : 160403338
                 * asset_id : gas
                 * name : Gas
                 * symbol : GAS
                 * rank : 50
                 * price_usd : 22.2248
                 * price_btc : 0.00225256
                 * volume_usd_24h : 1532410.0
                 * market_cap_usd : 191067392.0
                 * available_supply : 8597035.0
                 * total_supply : 11822072.0
                 * percent_change_1h : -2.01
                 * percent_change_24h : -3.31
                 * percent_change_7d : 0.4
                 * last_updated : 1511855363
                 * price_cny : 146.7725792
                 * volume_cny_24h : 10120035.64
                 * market_cap_cny : 1261809059.0
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

            public static class GntCategoryBean implements Serializable{
                /**
                 * id : 1
                 * category_id : 1
                 * name : SNT
                 * created_at : 2017-07-30 23:01:27
                 * updated_at : 2017-08-15 18:55:59
                 * icon : http://cryptobox.oss-cn-shenzhen.aliyuncs.com/7071bdd3-1366-3866-932b-6893a2e03a07.png
                 * address : 0xd9d700125b05f26df706f7190fa3be40c29af2fa
                 * gas : 11
                 * cap : {"id":12831458,"asset_id":"omisego","name":"OmiseGo","symbol":"OMG","rank":"14","price_usd":"6.49145","price_btc":"0.00152059","volume_usd_24h":"82698400.0","market_cap_usd":"638187588.0","available_supply":"98312024.0","total_supply":"140245398.0","percent_change_1h":"-0.37","percent_change_24h":"-8.19","percent_change_7d":"116.54","last_updated":"1502738361","price_cny":"43.28888347","volume_cny_24h":"551482550.24","market_cap_cny":"4255817751.0"}
                 */

                private String id;
                private int category_id;
                private String name;
                private String created_at;
                private String updated_at;
                private String icon;
                private String address;
                private String gas;
                private String decimals;
                private CapBean cap;

                public String getDecimals() {
                    return decimals;
                }

                public void setDecimals(String decimals) {
                    this.decimals = decimals;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
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

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getGas() {
                    return gas;
                }

                public void setGas(String gas) {
                    this.gas = gas;
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
    }

    public static class ListBean implements Serializable{
        /**
         * id : 6
         * user_id : 1
         * gnt_category_id : 1
         * name : SNT
         * created_at : 2017-08-16 00:32:24
         * updated_at : 2017-08-16 00:32:24
         * wallet_id : 22
         * decimals: 8
         * symbol: TSST
         * balance : 0x0000000000000000000000000000000000000000000000000000000000000000
         * gnt_category : {"id":1,"category_id":1,"name":"SNT","created_at":"2017-07-30 23:01:27","updated_at":"2017-08-15 18:55:59","icon":"http://cryptobox.oss-cn-shenzhen.aliyuncs.com/7071bdd3-1366-3866-932b-6893a2e03a07.png","address":"0xd9d700125b05f26df706f7190fa3be40c29af2fa","gas":"11","cap":{"id":12831458,"asset_id":"omisego","name":"OmiseGo","symbol":"OMG","rank":"14","price_usd":"6.49145","price_btc":"0.00152059","volume_usd_24h":"82698400.0","market_cap_usd":"638187588.0","available_supply":"98312024.0","total_supply":"140245398.0","percent_change_1h":"-0.37","percent_change_24h":"-8.19","percent_change_7d":"116.54","last_updated":"1502738361","price_cny":"43.28888347","volume_cny_24h":"551482550.24","market_cap_cny":"4255817751.0"}}
         * get_gas
         *
         */

        private int id;
        private int user_id;
        private int gnt_category_id;
        private String name;
        private String created_at;
        private String updated_at;
        private int wallet_id;
        private String balance;
        private String decimals;
        private String symbol;
        private String get_gas;
        private boolean isNeo;
        private GntCategoryBeanX gnt_category;
        private ArrayList<NewNeoTokenListBean> wallets;
        private int type;
        private int sort;

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public boolean isNeo() {
            return isNeo;
        }

        public void setNeo(boolean neo) {
            isNeo = neo;
        }

        public String getGet_gas() {
            return get_gas;
        }

        public void setGet_gas(String get_gas) {
            this.get_gas = get_gas;
        }

        public ArrayList<NewNeoTokenListBean> getWallets() {
            return wallets;
        }

        public void setWallets(ArrayList<NewNeoTokenListBean> wallets) {
            this.wallets = wallets;
        }

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

        public int getGnt_category_id() {
            return gnt_category_id;
        }

        public void setGnt_category_id(int gnt_category_id) {
            this.gnt_category_id = gnt_category_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public int getWallet_id() {
            return wallet_id;
        }

        public void setWallet_id(int wallet_id) {
            this.wallet_id = wallet_id;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public GntCategoryBeanX getGnt_category() {
            return gnt_category;
        }

        public void setGnt_category(GntCategoryBeanX gnt_category) {
            this.gnt_category = gnt_category;
        }

        public String getDecimals() {
            return decimals;
        }

        public void setDecimals(String decimals) {
            this.decimals = decimals;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public static class GntCategoryBeanX implements Serializable{
            /**
             * id : 1
             * category_id : 1
             * name : SNT
             * created_at : 2017-07-30 23:01:27
             * updated_at : 2017-08-15 18:55:59
             * icon : http://cryptobox.oss-cn-shenzhen.aliyuncs.com/7071bdd3-1366-3866-932b-6893a2e03a07.png
             * address : 0xd9d700125b05f26df706f7190fa3be40c29af2fa
             * gas : 11
             * cap : {"id":12831458,"asset_id":"omisego","name":"OmiseGo","symbol":"OMG","rank":"14","price_usd":"6.49145","price_btc":"0.00152059","volume_usd_24h":"82698400.0","market_cap_usd":"638187588.0","available_supply":"98312024.0","total_supply":"140245398.0","percent_change_1h":"-0.37","percent_change_24h":"-8.19","percent_change_7d":"116.54","last_updated":"1502738361","price_cny":"43.28888347","volume_cny_24h":"551482550.24","market_cap_cny":"4255817751.0"}
             */

            private int id;
            private int category_id;
            private String name;
            private String created_at;
            private String updated_at;
            private String icon;
            private String address;
            private String gas;
            private CapBeanX cap;

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

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getGas() {
                return gas;
            }

            public void setGas(String gas) {
                this.gas = gas;
            }

            public CapBeanX getCap() {
                return cap;
            }

            public void setCap(CapBeanX cap) {
                this.cap = cap;
            }

            public static class CapBeanX implements Serializable{

                /**
                 * id : omisego
                 * name : OmiseGO
                 * symbol : OMG
                 * rank : 21
                 * price_usd : 8.60111
                 * price_btc : 0.00057797
                 * 24h_volume_usd : 61617900.0
                 * market_cap_usd : 877679212.0
                 * available_supply : 102042552.0
                 * total_supply : 140245398.0
                 * max_supply : null
                 * percent_change_1h : 2.14
                 * percent_change_24h : -5.39
                 * percent_change_7d : 8.34
                 * last_updated : 1512652479
                 * price_cny : 56.911824648
                 * 24h_volume_cny : 407713320.72
                 * market_cap_cny : 5807427813.0
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
    }
}
