package com.inwecrypto.wallet.event;

import java.io.Serializable;

/**
 * 事件消息总类
 */
public class BaseEventBusBean<T> implements Serializable{

    private int eventCode = -1;

    private T data;

    private int key1;

    private int key2;

    public BaseEventBusBean(int eventCode) {
        this.eventCode = eventCode;
    }

    public BaseEventBusBean(int eventCode, T data) {
        this.eventCode = eventCode;
        this.data = data;
    }

    public BaseEventBusBean(int eventCode, int key1) {
        this.eventCode = eventCode;
        this.key1 = key1;
    }

    public BaseEventBusBean(int eventCode, int key1,int key2) {
        this.eventCode = eventCode;
        this.key1 = key1;
        this.key2 = key2;
    }

    public int getEventCode() {
        return eventCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getKey1() {
        return key1;
    }

    public void setKey1(int key1) {
        this.key1 = key1;
    }

    public int getKey2() {
        return key2;
    }

    public void setKey2(int key2) {
        this.key2 = key2;
    }
}
