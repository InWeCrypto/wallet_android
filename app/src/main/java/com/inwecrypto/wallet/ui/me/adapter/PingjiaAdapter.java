package com.inwecrypto.wallet.ui.me.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.PingjiaBean;
import com.inwecrypto.wallet.common.widget.RatingBar;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/4/3 17:48
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class PingjiaAdapter extends CommonAdapter<PingjiaBean> {
    public PingjiaAdapter(Context context, int layoutId, List<PingjiaBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, PingjiaBean pingjiaBean, int position) {
        holder.setText(R.id.name,pingjiaBean.getCategory().getName());
        holder.setText(R.id.long_name,"("+pingjiaBean.getCategory().getLong_name()+")");
        holder.setText(R.id.net,pingjiaBean.getCategory().getIndustry());
        Glide.with(mContext).load(pingjiaBean.getCategory().getImg()).crossFade().into((ImageView) holder.getView(R.id.img));
        RatingBar ratingBar=holder.getView(R.id.ratingbar);
        ratingBar.setStar(Float.parseFloat(pingjiaBean.getScore()));
        holder.setText(R.id.fenshu,pingjiaBean.getScore()+mContext.getString(R.string.fenshu));
    }
}
