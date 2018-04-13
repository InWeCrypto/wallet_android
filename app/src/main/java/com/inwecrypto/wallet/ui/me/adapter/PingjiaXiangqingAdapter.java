package com.inwecrypto.wallet.ui.me.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.PingjiaReplyBean;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import static com.inwecrypto.wallet.R.*;

/**
 * 作者：xiaoji06 on 2018/4/4 16:53
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class PingjiaXiangqingAdapter extends CommonAdapter<PingjiaReplyBean.DataBean> {
    public PingjiaXiangqingAdapter(Context context, int layoutId, List<PingjiaReplyBean.DataBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, PingjiaReplyBean.DataBean data, int position) {
        Glide.with(mContext)
                .load(data.getUser().getImg())
                .crossFade()
                .error(R.mipmap.wode_touxiang)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(id.img));
        holder.setText(R.id.name,data.getUser().getName());
        holder.setText(R.id.time, AppUtil.getGTime(data.getCreated_at()));
        holder.setText(id.content,data.getContent());
    }
}
