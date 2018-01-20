package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/1/11 16:01
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class IcoGas implements Serializable {
    private String gas_consumed;

    public String getGas_consumed() {
        return gas_consumed;
    }

    public void setGas_consumed(String gas_consumed) {
        this.gas_consumed = gas_consumed;
    }
}
