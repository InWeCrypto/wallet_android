package com.inwecrypto.wallet.ui.discover.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ArticleBean;

/**
 * Created by Administrator on 2017/8/3.
 * 功能描述：
 * 版本：@version
 */

public class ArticleAdapter extends CommonAdapter<ArticleBean> {


    public ArticleAdapter(Context context, int layoutId, List<ArticleBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ArticleBean articleBean, int position) {
        holder.setText(R.id.title,articleBean.getDetail().getTitle());
        holder.setText(R.id.time,articleBean.getCreated_at());
        holder.setText(R.id.desc,articleBean.getDetail().getDesc());
    }
}
