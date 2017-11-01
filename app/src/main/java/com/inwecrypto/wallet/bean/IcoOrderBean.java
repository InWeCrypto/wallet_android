package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class IcoOrderBean implements Serializable {

    private int id;//订单ID
    private int user_id;//付款人ID
    private int wallet_id;//钱包ID
    private String trade_no;//交易单号
    private String pay_address;//付款地址
    private String receive_address;//收款地址
    private String fee;//交易金额
    private String handle_fee;//手续费
    private String hash;
    private String created_at;//订单创建时间
    private Ico ico;//ico信息

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

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getPay_address() {
        return pay_address;
    }

    public void setPay_address(String pay_address) {
        this.pay_address = pay_address;
    }

    public String getReceive_address() {
        return receive_address;
    }

    public void setReceive_address(String receive_address) {
        this.receive_address = receive_address;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getHandle_fee() {
        return handle_fee;
    }

    public void setHandle_fee(String handle_fee) {
        this.handle_fee = handle_fee;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Ico getIco() {
        return ico;
    }

    public void setIco(Ico ico) {
        this.ico = ico;
    }

    public static class Ico implements Serializable{
        private String title;//标题
        private String cny;//币种

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCny() {
            return cny;
        }

        public void setCny(String cny) {
            this.cny = cny;
        }
    }
}
