package com.inwecrypto.wallet.ui.market.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.MarketAddBean;

/**
 * Created by Administrator on 2017/8/9.
 * 功能描述：
 * 版本：@version
 */

public class MarketAddAdapter extends CommonAdapter<MarketAddBean> {

    public MarketAddAdapter(Context context, int layoutId, List<MarketAddBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MarketAddBean marketBean, int position) {
        holder.setVisible(R.id.title,false);
        holder.setVisible(R.id.content,true);
        holder.setVisible(R.id.line,true);
        holder.setText(R.id.name,marketBean.getEn_name().toUpperCase());

        if (null==marketBean.getUser_ticker()){
            Glide.with(mContext).load(R.mipmap.list_btn_default).crossFade().into((ImageView) holder.getView(R.id.select));
        }else {
            Glide.with(mContext).load(R.mipmap.list_btn_selected).crossFade().into((ImageView) holder.getView(R.id.select));
        }
    }
}
