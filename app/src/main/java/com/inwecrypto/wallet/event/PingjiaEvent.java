package com.inwecrypto.wallet.event;

import java.io.Serializable;

/**
 * 作者：xiaoji06 on 2018/4/10 11:36
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class PingjiaEvent implements Serializable {

    public int position;

    public int up;

    public int down;

    public int smile;

    public int msg;

    public PingjiaEvent(int position,int up,int down,int smile,int msg){
        this.position=position;
        this.up=up;
        this.down=down;
        this.smile=smile;
        this.msg=msg;
    }
}
