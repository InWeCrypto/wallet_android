package com.inwecrypto.wallet.bean;

import java.io.Serializable;
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
        private Object deleted_at;
        private List<GntBean> gnt;

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

        public Object getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(Object deleted_at) {
            this.deleted_at = deleted_at;
        }

        public List<GntBean> getGnt() {
            return gnt;
        }

        public void setGnt(List<GntBean> gnt) {
            this.gnt = gnt;
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

            private int id;
            private int user_id;
            private int gnt_category_id;
            private String name;
            private String created_at;
            private String updated_at;
            private int wallet_id;
            private String balance;
            private GntCategoryBean gnt_category;

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

            public GntCategoryBean getGnt_category() {
                return gnt_category;
            }

            public void setGnt_category(GntCategoryBean gnt_category) {
                this.gnt_category = gnt_category;
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

                private int id;
                private int category_id;
                private String name;
                private String created_at;
                private String updated_at;
                private String icon;
                private String address;
                private String gas;
                private CapBean cap;

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
         * balance : 0x0000000000000000000000000000000000000000000000000000000000000000
         * gnt_category : {"id":1,"category_id":1,"name":"SNT","created_at":"2017-07-30 23:01:27","updated_at":"2017-08-15 18:55:59","icon":"http://cryptobox.oss-cn-shenzhen.aliyuncs.com/7071bdd3-1366-3866-932b-6893a2e03a07.png","address":"0xd9d700125b05f26df706f7190fa3be40c29af2fa","gas":"11","cap":{"id":12831458,"asset_id":"omisego","name":"OmiseGo","symbol":"OMG","rank":"14","price_usd":"6.49145","price_btc":"0.00152059","volume_usd_24h":"82698400.0","market_cap_usd":"638187588.0","available_supply":"98312024.0","total_supply":"140245398.0","percent_change_1h":"-0.37","percent_change_24h":"-8.19","percent_change_7d":"116.54","last_updated":"1502738361","price_cny":"43.28888347","volume_cny_24h":"551482550.24","market_cap_cny":"4255817751.0"}}
         */

        private int id;
        private int user_id;
        private int gnt_category_id;
        private String name;
        private String created_at;
        private String updated_at;
        private int wallet_id;
        private String balance;
        private GntCategoryBeanX gnt_category;

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
        }
    }
}
