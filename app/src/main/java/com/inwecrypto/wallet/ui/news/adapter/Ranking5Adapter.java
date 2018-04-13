package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.Rank4Bean;
import com.inwecrypto.wallet.bean.Rank5Bean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/4/2 14:39
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class Ranking5Adapter extends CommonAdapter<Rank5Bean.DataBean>{
    public Ranking5Adapter(Context context, int layoutId, List<Rank5Bean.DataBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Rank5Bean.DataBean rank5Bean, int position) {
        holder.setText(R.id.title,rank5Bean.getTitle());
        holder.setText(R.id.hot,rank5Bean.getClick_rate()+"");
    }
}
