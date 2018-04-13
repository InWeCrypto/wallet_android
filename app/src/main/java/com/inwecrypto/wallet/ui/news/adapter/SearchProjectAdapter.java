package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ProjectDetaileBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/12 20:05
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class SearchProjectAdapter extends CommonAdapter<ProjectDetaileBean>{
    public SearchProjectAdapter(Context context, int layoutId, List<ProjectDetaileBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ProjectDetaileBean projectDetaileBean, int position) {
        holder.setText(R.id.unit,projectDetaileBean.getName());
        holder.setText(R.id.name,projectDetaileBean.getLong_name());
        holder.setText(R.id.block_chain,projectDetaileBean.getIndustry());
        Glide.with(mContext)
                .load(projectDetaileBean.getImg())
                .crossFade()
                .into((ImageView) holder.getView(R.id.img));
    }
}
