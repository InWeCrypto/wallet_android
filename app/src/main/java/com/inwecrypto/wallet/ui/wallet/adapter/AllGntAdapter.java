package com.inwecrypto.wallet.ui.wallet.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.GntBean;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;

/**
 * Created by Administrator on 2017/8/10.
 * 功能描述：
 * 版本：@version
 */

public class AllGntAdapter extends CommonAdapter<GntBean> {

    public AllGntAdapter(Context context, int layoutId, List<GntBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, GntBean gntBean, int position) {
        Glide.with(mContext).load(gntBean.getIcon()).crossFade().into((ImageView) holder.getView(R.id.img));
        holder.setText(R.id.name,gntBean.getName());
        if (!gntBean.isSelect()){
            Glide.with(mContext).load(R.mipmap.newneonoselectxxhdpi).crossFade().into((ImageView) holder.getView(R.id.select));
        }else {
            Glide.with(mContext).load(R.mipmap.newneoselectxxhdpi).crossFade().into((ImageView) holder.getView(R.id.select));
        }
    }
}
