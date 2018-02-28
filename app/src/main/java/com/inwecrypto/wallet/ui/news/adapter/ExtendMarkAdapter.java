package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.CommonProjectBean;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import static com.inwecrypto.wallet.R.id.img;

/**
 * 作者：xiaoji06 on 2018/2/8 11:39
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ExtendMarkAdapter extends CommonAdapter<CommonProjectBean> {

    private final int width;
    private FrameLayout.LayoutParams params;

    public ExtendMarkAdapter(Context context, int layoutId, List<CommonProjectBean> datas) {
        super(context, layoutId, datas);
        width=ScreenUtils.getScreenWidth(mContext)/4;
        params=new FrameLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void convert(ViewHolder holder, CommonProjectBean commonProjectBean, int position) {
        holder.getView(R.id.bg).setLayoutParams(params);
        holder.setText(R.id.name,mContext.getString(commonProjectBean.getName()));
        Glide.with(mContext).load(commonProjectBean.getImg()).crossFade().into((ImageView) holder.getView(img));
    }
}
