package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.SearchBean;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/12 20:14
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class SearchNewsAdapter extends CommonAdapter<SearchBean.DataBean> {

    public SearchNewsAdapter(Context context, int layoutId, List<SearchBean.DataBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, SearchBean.DataBean dataBean, int position) {
        holder.setText(R.id.title,dataBean.getTitle());
        holder.setText(R.id.time, AppUtil.getGTime(dataBean.getCreated_at()));
    }
}
