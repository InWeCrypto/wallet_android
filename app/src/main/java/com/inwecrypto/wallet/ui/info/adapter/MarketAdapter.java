package com.inwecrypto.wallet.ui.info.adapter;

import android.content.Context;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.InfoMarketBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2017/11/17 11:48
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class MarketAdapter extends CommonAdapter<InfoMarketBean> {
    public MarketAdapter(Context context, int layoutId, List<InfoMarketBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, InfoMarketBean infoMarketBean, int position) {
        holder.setText(R.id.pingtai,infoMarketBean.getSource());
        holder.setText(R.id.jiaoyidui,"("+infoMarketBean.getPair()+")");
        holder.setText(R.id.jiage,"≈"+infoMarketBean.getPairce());
        holder.setText(R.id.chengjiaoliang24h,infoMarketBean.getVolum_24());
        holder.setText(R.id.gengxinshijian,infoMarketBean.getUpdate());
        if (position!=mDatas.size()-1){
            holder.setVisible(R.id.line,true);
        }else {
            holder.setVisible(R.id.line,false);
        }
    }
}
