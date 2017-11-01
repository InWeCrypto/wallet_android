package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class LocalWalletListBean implements Serializable {

    private ArrayList<WalletBean> list;

    public ArrayList<WalletBean> getList() {
        return list;
    }

    public void setList(ArrayList<WalletBean> list) {
        this.list = list;
    }
}
