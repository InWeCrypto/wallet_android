package com.inwecrypto.wallet.ui.wallet.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.MessageBean;

/**
 * Created by Administrator on 2017/8/14.
 * 功能描述：
 * 版本：@version
 */

public class MessageAdapter extends CommonAdapter<MessageBean> {
    public MessageAdapter(Context context, int layoutId, List<MessageBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MessageBean messageBean, int position) {
        holder.setText(R.id.title,messageBean.getTitle());
        holder.setText(R.id.message,messageBean.getContent());
        holder.setText(R.id.time,messageBean.getCreated_at());
    }
}
