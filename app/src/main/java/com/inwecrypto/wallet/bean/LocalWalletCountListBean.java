package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class LocalWalletCountListBean implements Serializable {

    private ArrayList<WalletCountBean> list;

    public ArrayList<WalletCountBean> getList() {
        return list;
    }

    public void setList(ArrayList<WalletCountBean> list) {
        this.list = list;
    }
}
