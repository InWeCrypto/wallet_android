package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xiaoji06 on 2017/11/25 16:51
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class UtxoBean implements Serializable {


    /**
     * jsonrpc : 2.0
     * result : [{"txid":"0x2029a44ee429ed60f217de9129d768df7502f4ea7028bab09bbaa1539a87a76c","vout":{"Address":"AMpupnF6QweQXLfCtF4dR45FDdKbTXkLsr","Asset":"0xc56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b","N":0,"Value":"1"},"createTime":"2017-11-28T05:59:05Z","spentTime":"","block":823940,"spentBlock":-1}]
     * id : 0
     */

    private String jsonrpc;
    private int id;
    private List<ResultBean> result;
    private ErrorBean error;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public static class ResultBean implements Serializable{
        /**
         * txid : 0x2029a44ee429ed60f217de9129d768df7502f4ea7028bab09bbaa1539a87a76c
         * vout : {"Address":"AMpupnF6QweQXLfCtF4dR45FDdKbTXkLsr","Asset":"0xc56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b","N":0,"Value":"1"}
         * createTime : 2017-11-28T05:59:05Z
         * spentTime :
         * block : 823940
         * spentBlock : -1
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

    public static class ErrorBean implements Serializable{

        /**
         * code : -32603
         * message : get AMpupnF6QweQXLfCtF4dR45FDdKbTXkLsr balance 0xc56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b err: pq: there is no parameter $1
         * data : null
         */

        private int code;
        private String message;
        private String data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
