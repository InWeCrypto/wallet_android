package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ArticleDetaileBean;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/10 10:32
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class InwehotAdapter extends CommonAdapter<ArticleDetaileBean> {

    private LinearLayout.LayoutParams params;

    public InwehotAdapter(Context context, int layoutId, List<ArticleDetaileBean> datas) {
        super(context, layoutId, datas);
        int hight= (int) ((ScreenUtils.getScreenWidth(mContext)- DensityUtil.dip2px(mContext,30))/690.0*333.0);
        params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,hight);
    }

    @Override
    protected void convert(ViewHolder holder, ArticleDetaileBean articleDetaileBean, int position) {
        holder.setText(R.id.time, AppUtil.getGTime(articleDetaileBean.getCreated_at()));
        holder.setText(R.id.title,articleDetaileBean.getTitle());

        holder.getView(R.id.img).setLayoutParams(params);
        if (null!=articleDetaileBean.getImg()){
            Glide.with(mContext)
                    .load(articleDetaileBean.getImg())
                    .placeholder(R.mipmap.zhanweitu_ico)
                    .crossFade()
                    .into((ImageView) holder.getView(R.id.img));
        }else {
            Glide.with(mContext)
                    .load(R.mipmap.zhanweitu_ico)
                    .crossFade()
                    .into((ImageView) holder.getView(R.id.img));
        }
    }
}
