package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/13.
 * 功能描述：
 * 版本：@version
 */

public class ValueBean implements Serializable {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
