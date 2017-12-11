package com.inwecrypto.wallet.ui.market.adapter;

import android.content.Context;
import android.graphics.Color;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.DecimalFormat;
import java.util.List;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.MarkeListBean;

/**
 * Created by Administrator on 2017/8/9.
 * 功能描述：
 * 版本：@version
 */

public class MarketAdapter extends CommonAdapter<MarkeListBean> {

    DecimalFormat decimalFormat;

    public MarketAdapter(Context context, int layoutId, List<MarkeListBean> datas) {
        super(context, layoutId, datas);
        decimalFormat =new DecimalFormat("0.00");
    }

    @Override
    protected void convert(ViewHolder holder, MarkeListBean marketBean, int position) {
        holder.setText(R.id.name,marketBean.getEn_name().toUpperCase());
        holder.setText(R.id.net_name,"");

        if (null!=marketBean.getTime_data()){
            holder.setText(R.id.price_us,"$"+decimalFormat.format(Float.parseFloat(marketBean.getTime_data().getPrice_usd())));
            holder.setText(R.id.price_cn,"￥"+decimalFormat.format(Float.parseFloat(marketBean.getTime_data().getPrice_cny())));
            if (marketBean.getTime_data().getChange_24h().contains("-")){
                holder.setText(R.id.pers,marketBean.getTime_data().getChange_24h()+"%");
                holder.setBackgroundColor(R.id.pers, Color.parseColor("#E86438"));
            }else {
                holder.setText(R.id.pers,"+"+marketBean.getTime_data().getChange_24h()+"%");
                holder.setBackgroundColor(R.id.pers, Color.parseColor("#008C55"));
            }
            holder.setText(R.id.gao,"$"+decimalFormat.format(Float.parseFloat(marketBean.getTime_data().getMax_price_cny_24h())));
            holder.setText(R.id.di,"$"+decimalFormat.format(Float.parseFloat(marketBean.getTime_data().getMin_price_cny_24h())));
            float liang=Float.parseFloat(marketBean.getTime_data().getVolume_usd_24h());
            if (liang<10000){
                holder.setText(R.id.liang,"$"+decimalFormat.format(liang));
            }else if (liang<100000000){
                holder.setText(R.id.liang,"$"+decimalFormat.format(liang/10000)+mContext.getString(R.string.wan));
            }else {
                holder.setText(R.id.liang,"$"+decimalFormat.format(liang/10000/10000)+mContext.getString(R.string.yi));
            }
        }else {
            holder.setText(R.id.pers,"0%");
            holder.setBackgroundColor(R.id.pers, Color.parseColor("#008C55"));
            holder.setText(R.id.price_us,"$0.00");
            holder.setText(R.id.price_cn,"￥0.00");
            holder.setText(R.id.gao,"$0.00");
            holder.setText(R.id.di,"$0.00");
            holder.setText(R.id.liang,"$0.00");
        }

    }
}
