package com.inwecrypto.wallet.ui.me.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ProjectDetaileBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/7 17:08
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class MarketTipAdapter extends CommonAdapter<ProjectDetaileBean> {
    public MarketTipAdapter(Context context, int layoutId, List<ProjectDetaileBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ProjectDetaileBean projectDetaileBean, int position) {
        if (null!=projectDetaileBean.getImg()){
            Glide.with(mContext)
                    .load(projectDetaileBean.getImg())
                    .crossFade()
                    .into((ImageView) holder.getView(R.id.img));
        }
        holder.setText(R.id.name,projectDetaileBean.getName());
        holder.setText(R.id.smbyle,"("+projectDetaileBean.getLong_name()+")");
        if (null!=projectDetaileBean.getIco()){
            if (App.get().getUnit()==1){
                holder.setText(R.id.price,"¥"+projectDetaileBean.getIco().getPrice_cny());
            }else {
                holder.setText(R.id.price,"$"+projectDetaileBean.getIco().getPrice_usd());
            }
            holder.setText(R.id.charge,"("+projectDetaileBean.getIco().getPercent_change_24h()+"%)");
            if (projectDetaileBean.getIco().getPercent_change_24h().contains("-")){
                holder.setText(R.id.charge,"("+projectDetaileBean.getIco().getPercent_change_24h()+"%)");
                ((TextView)holder.getView(R.id.charge)).setTextColor(Color.parseColor("#FF680F"));
            }else {
                holder.setText(R.id.charge,"(+"+projectDetaileBean.getIco().getPercent_change_24h()+"%)");
                ((TextView)holder.getView(R.id.charge)).setTextColor(Color.parseColor("#008C55"));
            }
        }else {
            holder.setText(R.id.name,"");
            holder.setText(R.id.smbyle,"()");
            if (App.get().getUnit()==1){
                holder.setText(R.id.price,"¥----");
            }else {
                holder.setText(R.id.price,"$----");
            }
            holder.setText(R.id.charge,"(--)");

        }

        if (null!=projectDetaileBean.getCategory_user()){
            holder.setText(R.id.hight,mContext.getString(R.string.jiageshangxian_me)+" $"+projectDetaileBean.getCategory_user().getMarket_hige());
            holder.setText(R.id.low,mContext.getString(R.string.jiagexiaxian_me)+" $"+projectDetaileBean.getCategory_user().getMarket_lost());
        }else {
            holder.setText(R.id.hight,mContext.getString(R.string.jiageshangxian_me)+" $----");
            holder.setText(R.id.low,mContext.getString(R.string.jiagexiaxian_me)+" $----");
        }
    }
}
