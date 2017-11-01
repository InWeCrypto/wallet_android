package com.inwecrypto.wallet.event;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/10.
 * 功能描述：
 * 版本：@version
 */

public class KeyEvent implements Serializable  {
    private String key;

    public KeyEvent(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
