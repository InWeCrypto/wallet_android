package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/30.
 * 功能描述：
 * 版本：@version
 */

public class BpsBean implements Serializable {
    private float bps;

    public float getBps() {
        return bps;
    }

    public void setBps(float bps) {
        this.bps = bps;
    }
}
