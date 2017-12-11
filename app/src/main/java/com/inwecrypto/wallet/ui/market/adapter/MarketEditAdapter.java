package com.inwecrypto.wallet.ui.market.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.MarkeListBean;

/**
 * Created by Administrator on 2017/8/9.
 * 功能描述：
 * 版本：@version
 */

public class MarketEditAdapter extends CommonAdapter<MarkeListBean> {

    public MarketEditAdapter(Context context, int layoutId, List<MarkeListBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MarkeListBean marketBean, int position) {
        holder.setText(R.id.name,marketBean.getEn_name().toUpperCase());
        holder.setText(R.id.net_name,"");
    }
}
