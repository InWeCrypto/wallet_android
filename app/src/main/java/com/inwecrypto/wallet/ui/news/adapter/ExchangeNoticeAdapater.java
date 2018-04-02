package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ExchangeNoticeBean;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/10 14:38
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ExchangeNoticeAdapater extends CommonAdapter<ExchangeNoticeBean>{
    public ExchangeNoticeAdapater(Context context, int layoutId, List<ExchangeNoticeBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ExchangeNoticeBean exchangeNoticeBean, int position) {
        holder.setText(R.id.time,AppUtil.getGTime(exchangeNoticeBean.getCreated_at()));
        holder.setText(R.id.from,null==exchangeNoticeBean.getSource_name()?"":exchangeNoticeBean.getSource_name());
        holder.setText(R.id.order_time,AppUtil.getGTime(exchangeNoticeBean.getCreated_at()));
        holder.setText(R.id.content, exchangeNoticeBean.getContent());

    }
}
