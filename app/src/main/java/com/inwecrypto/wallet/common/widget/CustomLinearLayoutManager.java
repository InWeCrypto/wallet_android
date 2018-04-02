package com.inwecrypto.wallet.common.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * 作者：xiaoji06 on 2018/3/21 17:12
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
