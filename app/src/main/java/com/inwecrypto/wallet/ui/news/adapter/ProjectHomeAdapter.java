package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.TradingProjectDetaileBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/11 17:07
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectHomeAdapter extends CommonAdapter<TradingProjectDetaileBean.CategoryMediaBean> {

    public ProjectHomeAdapter(Context context, int layoutId, List<TradingProjectDetaileBean.CategoryMediaBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TradingProjectDetaileBean.CategoryMediaBean categoryMediaBean, int position) {
        if (position==(mDatas.size()-1)){
            holder.getView(R.id.line).setVisibility(View.INVISIBLE);
        }else {
            holder.getView(R.id.line).setVisibility(View.VISIBLE);
        }

        holder.setText(R.id.name,categoryMediaBean.getName());
        if (null!=categoryMediaBean.getImg()){
            Glide.with(mContext)
                    .load(categoryMediaBean.getImg())
                    .crossFade()
                    .into((ImageView) holder.getView(R.id.img));
        }
    }
}
