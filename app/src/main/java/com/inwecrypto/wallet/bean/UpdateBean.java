package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/1/29 18:14
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class UpdateBean implements Serializable {

    /**
     * versionCode : 20180129
     * updateHit :
     * hash :
     * force : 0
     */

    private String versionCode;
    private String updateHit;
    private String hash;
    private String force;

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getUpdateHit() {
        return updateHit;
    }

    public void setUpdateHit(String updateHit) {
        this.updateHit = updateHit;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }
}
