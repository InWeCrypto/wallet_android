package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class CommonDataBean<T> implements Serializable{

    private T data;

    public T getRecord() {
        return data;
    }

    public void setRecord(T data) {
        this.data = data;
    }
}
