package com.inwecrypto.wallet.ui.me.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
        if (null!=projectDetaileBean.getIco()){
            holder.setText(R.id.name,projectDetaileBean.getIco().getName());
            holder.setText(R.id.smbyle,"("+projectDetaileBean.getIco().getSymbol()+")");
            holder.setText(R.id.price,"$"+projectDetaileBean.getIco().getPrice_usd());
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
            holder.setText(R.id.price,"$----");
            holder.setText(R.id.charge,"(--)");

        }

        if (null!=projectDetaileBean.getCategory_user()){
            holder.setText(R.id.hight,mContext.getString(R.string.above)+" $"+projectDetaileBean.getCategory_user().getMarket_hige());
            holder.setText(R.id.low,mContext.getString(R.string.above)+" $"+projectDetaileBean.getCategory_user().getMarket_lost());
        }else {
            holder.setText(R.id.hight,mContext.getString(R.string.above)+" $----");
            holder.setText(R.id.low,mContext.getString(R.string.above)+" $----");
        }
    }
}
