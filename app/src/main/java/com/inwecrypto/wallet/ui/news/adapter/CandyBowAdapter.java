package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.CandyBowBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/10 16:27
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class CandyBowAdapter extends CommonAdapter<CandyBowBean.ListBean.DataBean> {
    public CandyBowAdapter(Context context, int layoutId, List<CandyBowBean.ListBean.DataBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, CandyBowBean.ListBean.DataBean dataBean, int position) {
        holder.setText(R.id.title,dataBean.getName());
        holder.setText(R.id.content,dataBean.getDesc());
        if (null!=holder.getView(R.id.img)){
            if (null!=dataBean.getImg()){
                Glide.with(mContext)
                        .load(dataBean.getImg())
                        .crossFade()
                        .into((ImageView) holder.getView(R.id.img));
            }else {
                Glide.with(mContext)
                        .load("")
                        .crossFade()
                        .into((ImageView) holder.getView(R.id.img));
            }
        }
    }
}
