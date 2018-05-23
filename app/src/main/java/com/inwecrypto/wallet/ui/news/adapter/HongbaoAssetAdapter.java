package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.HongbaoGntBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/4/25 14:35
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class HongbaoAssetAdapter extends CommonAdapter<HongbaoGntBean> {

    public HongbaoAssetAdapter(Context context, int layoutId, List<HongbaoGntBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, HongbaoGntBean data, int position) {
        Glide.with(mContext).load(data.getIcon()).crossFade().into((ImageView) holder.getView(R.id.img));
        holder.setText(R.id.name,data.getName());
    }
}
