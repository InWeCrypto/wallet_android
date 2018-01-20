package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/1/12 12:57
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NewNeoGntInfoBean implements Serializable {


    /**
     * decimals : 8
     * symbol : TSST
     * balance : 0
     */

    private String decimals;
    private String symbol;
    private String balance;

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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
