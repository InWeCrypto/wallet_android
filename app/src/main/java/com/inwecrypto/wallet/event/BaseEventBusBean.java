package com.inwecrypto.wallet.event;

import java.io.Serializable;

/**
 * 事件消息总类
 */
public class BaseEventBusBean<T> implements Serializable{

    private int eventCode = -1;

    private T data;

    public BaseEventBusBean(int eventCode) {
        this.eventCode = eventCode;
    }

    public BaseEventBusBean(int eventCode, T data) {
        this.eventCode = eventCode;
        this.data = data;
    }

    public int getEventCode() {
        return eventCode;
    }

    public T getData() {
        return data;
    }
}
