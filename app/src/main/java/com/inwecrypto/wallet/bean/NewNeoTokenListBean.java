package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/1/10 17:50
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NewNeoTokenListBean implements Serializable {

    private String name;
    private String price;
    private int id;
    private WalletBean wallet;

    public NewNeoTokenListBean(String name,String price,int id,WalletBean wallet){
        this.name=name;
        this.price=price;
        this.id=id;
        this.wallet=wallet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WalletBean getWallet() {
        return wallet;
    }

    public void setWallet(WalletBean wallet) {
        this.wallet = wallet;
    }
}
