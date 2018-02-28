package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ProjectMarketBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/11 15:44
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class TradingMarketAdapter extends CommonAdapter<ProjectMarketBean> {
    public TradingMarketAdapter(Context context, int layoutId, List<ProjectMarketBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ProjectMarketBean projectMarketBean, int position) {
        holder.setText(R.id.name,projectMarketBean.getSource()+"("+projectMarketBean.getPair()+")");
        holder.setText(R.id.price,projectMarketBean.getPairce());
        holder.setText(R.id.volume,"Volume (24h)："+projectMarketBean.getVolum_24());
    }
}
