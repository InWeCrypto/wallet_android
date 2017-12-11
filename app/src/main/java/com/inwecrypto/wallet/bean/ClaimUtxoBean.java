package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xiaoji06 on 2017/11/25 16:53
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ClaimUtxoBean implements Serializable {

    /**
     * jsonrpc : 2.0
     * result : []
     * id : 0
     */

    private String jsonrpc;
    private int id;
    private GasUtxoBean result;


    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GasUtxoBean getResult() {
        return result;
    }

    public void setResult(GasUtxoBean result) {
        this.result = result;
    }


    public static class GasUtxoBean implements Serializable{
        /**
         * Unavailable : 0.00000032
         * Available : 0.00000736
         * Claims : [{"txid":"0xc503927ac7541c45578e975d4971f2e5cad951f33a591bb59e9303548bf65fe3","vout":{"Address":"AMpupnF6QweQXLfCtF4dR45FDdKbTXkLsr","Asset":"0xc56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b","N":0,"Value":"1"},"createTime":"2017-11-24T06:38:04Z","spentTime":"2017-11-24T07:18:12Z","block":811513,"spentBlock":811605}]
         */

        private String Unavailable;
        private String Available;
        private List<ClaimsBean> Claims;

        public String getUnavailable() {
            return Unavailable;
        }

        public void setUnavailable(String unavailable) {
            Unavailable = unavailable;
        }

        public String getAvailable() {
            return Available;
        }

        public void setAvailable(String available) {
            Available = available;
        }

        public List<ClaimsBean> getClaims() {
            return Claims;
        }

        public void setClaims(List<ClaimsBean> claims) {
            Claims = claims;
        }

        public static class ClaimsBean implements Serializable{
            /**
             * txid : 0xc503927ac7541c45578e975d4971f2e5cad951f33a591bb59e9303548bf65fe3
             * vout : {"Address":"AMpupnF6QweQXLfCtF4dR45FDdKbTXkLsr","Asset":"0xc56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b","N":0,"Value":"1"}
             * createTime : 2017-11-24T06:38:04Z
             * spentTime : 2017-11-24T07:18:12Z
             * block : 811513
             * spentBlock : 811605
             */

            private String txid;
            private VoutBean vout;
            private String createTime;
            private String spentTime;
            private int block;
            private int spentBlock;

            public String getTxid() {
                return txid;
            }

            public void setTxid(String txid) {
                this.txid = txid;
            }

            public VoutBean getVout() {
                return vout;
            }

            public void setVout(VoutBean vout) {
                this.vout = vout;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getSpentTime() {
                return spentTime;
            }

            public void setSpentTime(String spentTime) {
                this.spentTime = spentTime;
            }

            public int getBlock() {
                return block;
            }

            public void setBlock(int block) {
                this.block = block;
            }

            public int getSpentBlock() {
                return spentBlock;
            }

            public void setSpentBlock(int spentBlock) {
                this.spentBlock = spentBlock;
            }

            public static class VoutBean implements Serializable{
                /**
                 * Address : AMpupnF6QweQXLfCtF4dR45FDdKbTXkLsr
                 * Asset : 0xc56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b
                 * N : 0
                 * Value : 1
                 */

                private String Address;
                private String Asset;
                private int N;
                private String Value;

                public String getAddress() {
                    return Address;
                }

                public void setAddress(String Address) {
                    this.Address = Address;
                }

                public String getAsset() {
                    return Asset;
                }

                public void setAsset(String Asset) {
                    this.Asset = Asset;
                }

                public int getN() {
                    return N;
                }

                public void setN(int N) {
                    this.N = N;
                }

                public String getValue() {
                    return Value;
                }

                public void setValue(String Value) {
                    this.Value = Value;
                }
            }
        }
    }

}
