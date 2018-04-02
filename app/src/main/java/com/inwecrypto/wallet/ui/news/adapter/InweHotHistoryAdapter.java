package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ArticleDetaileBean;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/10 11:39
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class InweHotHistoryAdapter extends CommonAdapter<ArticleDetaileBean> {
    public InweHotHistoryAdapter(Context context, int layoutId, List<ArticleDetaileBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ArticleDetaileBean articleDetaileBean, int position) {
        holder.setText(R.id.title,articleDetaileBean.getTitle());
        holder.setText(R.id.time, AppUtil.getGTime(articleDetaileBean.getCreated_at()));
        if (articleDetaileBean.isIs_sole()){
            holder.setVisible(R.id.yuanchuang,true);
        }else {
            holder.setVisible(R.id.yuanchuang,false);
        }
        if (null!=articleDetaileBean.getImg()){
            Glide.with(mContext)
                    .load(articleDetaileBean.getImg())
                    .crossFade()
                    .error(R.mipmap.zhanweitu_ico)
                    .into((ImageView) holder.getView(R.id.img));
        }else {
            Glide.with(mContext)
                    .load(R.mipmap.zhanweitu_ico)
                    .crossFade()
                    .into((ImageView) holder.getView(R.id.img));
        }
    }
}
