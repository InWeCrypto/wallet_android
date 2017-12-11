package com.inwecrypto.wallet.ui.info.adapter;

import android.content.Context;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ProjectBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2017/11/16 20:48
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ExplorerAdapter extends CommonAdapter<ProjectBean.ProjectExplorersBean> {

    public ExplorerAdapter(Context context, int layoutId, List<ProjectBean.ProjectExplorersBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ProjectBean.ProjectExplorersBean projectExplorersBean, int position) {
        holder.setText(R.id.title,projectExplorersBean.getName());
    }
}
