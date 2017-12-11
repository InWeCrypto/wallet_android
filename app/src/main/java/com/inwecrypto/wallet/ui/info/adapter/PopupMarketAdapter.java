package com.inwecrypto.wallet.ui.info.adapter;

import android.content.Context;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ProjectBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2017/11/16 14:57
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class PopupMarketAdapter extends CommonAdapter<ProjectBean.ProjectMarketsBean> {

    public PopupMarketAdapter(Context context, int layoutId, List<ProjectBean.ProjectMarketsBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ProjectBean.ProjectMarketsBean projectMarketsBean, int position) {
        holder.setText(R.id.name,projectMarketsBean.getName());
        if (position==(mDatas.size()-1)){
            holder.setVisible(R.id.line,false);
        }else {
            holder.setVisible(R.id.line,true);
        }
    }
}
