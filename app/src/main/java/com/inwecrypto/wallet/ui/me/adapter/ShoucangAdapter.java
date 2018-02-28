package com.inwecrypto.wallet.ui.me.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ArticleDetaileBean;
import com.inwecrypto.wallet.bean.ProjectDetaileBean;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/7 14:52
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ShoucangAdapter extends CommonAdapter<ArticleDetaileBean> {

    public ShoucangAdapter(Context context, int layoutId, List<ArticleDetaileBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ArticleDetaileBean articleDetaileBean, int position) {
        holder.setText(R.id.title,articleDetaileBean.getTitle());
        holder.setText(R.id.time,articleDetaileBean.getCreated_at());
        if (null!=articleDetaileBean.getImg()){
            Glide.with(mContext)
                    .load(articleDetaileBean.getImg())
                    .crossFade()
                    .into((ImageView) holder.getView(R.id.img));
        }
    }
}
