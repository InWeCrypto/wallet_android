package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.NoticeBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/24 17:23
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NoticeAdapter extends CommonAdapter<NoticeBean> {
    public NoticeAdapter(Context context, int layoutId, List<NoticeBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, NoticeBean noticeBean, int position) {
        holder.setText(R.id.time,noticeBean.getTime());
        holder.setText(R.id.order_time,noticeBean.getTime());
        holder.setText(R.id.from,noticeBean.getTitle());
        holder.setText(R.id.content,noticeBean.getContent());
    }
}
