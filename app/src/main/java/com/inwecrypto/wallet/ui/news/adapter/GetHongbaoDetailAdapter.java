package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.HongbaoDetailBean;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 作者：xiaoji06 on 2018/4/24 15:24
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class GetHongbaoDetailAdapter extends CommonAdapter<HongbaoDetailBean.DrawsBean> {

    private String address;
    private String symbol;
    private boolean isOpen;
    private String decimal="18";

    public GetHongbaoDetailAdapter(Context context, int layoutId, List<HongbaoDetailBean.DrawsBean> datas,String address,String symbol,boolean isOpen) {
        super(context, layoutId, datas);
        this.address=address;
        this.symbol=symbol;
        this.isOpen=isOpen;
    }

    @Override
    protected void convert(ViewHolder holder, HongbaoDetailBean.DrawsBean data, int position) {
        if (!address.toLowerCase().equals(data.getDraw_addr().toLowerCase())){
            ((TextView)holder.getView(R.id.address)).setTextColor(Color.parseColor("#333333"));
            ((TextView)holder.getView(R.id.num)).setTextColor(Color.parseColor("#333333"));
            ((TextView)holder.getView(R.id.time)).setTextColor(Color.parseColor("#333333"));
        }else {
            ((TextView)holder.getView(R.id.address)).setTextColor(Color.parseColor("#4A90E2"));
            ((TextView)holder.getView(R.id.num)).setTextColor(Color.parseColor("#4A90E2"));
            ((TextView)holder.getView(R.id.time)).setTextColor(Color.parseColor("#4A90E2"));
        }
        holder.setText(R.id.address,data.getDraw_addr());
        if (isOpen){
            String price=new BigDecimal(AppUtil.toD(data.getValue().replace("0x","0"))).divide(AppUtil.decimal(decimal),4, RoundingMode.DOWN).toPlainString();
            holder.setText(R.id.num,price+symbol);
        }else {
            holder.setText(R.id.num,"***"+symbol);
        }
        holder.setText(R.id.time, AppUtil.getGTime(data.getCreated_at()));
    }

    public void setIsOpen(boolean isOpen){
        this.isOpen=isOpen;
    }

    public void setDecimal(String decimal){
        this.decimal=decimal;
    }
}
