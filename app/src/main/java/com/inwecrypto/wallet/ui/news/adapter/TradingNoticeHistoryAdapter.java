package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;

import com.inwecrypto.wallet.bean.TradingNoticeBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/22 17:19
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class TradingNoticeHistoryAdapter extends CommonAdapter<TradingNoticeBean> {
    public TradingNoticeHistoryAdapter(Context context, int layoutId, List<TradingNoticeBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TradingNoticeBean tradingNoticeBean, int position) {

    }
}
