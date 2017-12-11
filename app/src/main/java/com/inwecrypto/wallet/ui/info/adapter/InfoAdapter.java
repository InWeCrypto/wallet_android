package com.inwecrypto.wallet.ui.info.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.NewsBean;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.widget.MultiItemCommonAdapter;
import com.inwecrypto.wallet.common.widget.MultiItemTypeSupport;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2017/11/14 15:24
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class InfoAdapter extends MultiItemCommonAdapter<NewsBean> {

    private int width;
    private FrameLayout.LayoutParams params;

    public InfoAdapter(Context context, List<NewsBean> datas, MultiItemTypeSupport<NewsBean> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);
        width= ScreenUtils.getScreenWidth(context)- DensityUtil.dip2px(context,30);
        params=new FrameLayout.LayoutParams(width, (int) (width/690*406));
    }

    @Override
    protected void convert(final ViewHolder holder, NewsBean infoBean, int position) {
        switch (infoBean.getType()){
            case 1:
                holder.setText(R.id.title,mContext.getString(R.string.kuaixun)+(infoBean.getTitle()==null?"":infoBean.getTitle()));
                break;
            case 2:
            case 3:
                holder.getView(R.id.img).setLayoutParams(params);
                holder.setText(R.id.title,infoBean.getTitle()==null?"":infoBean.getTitle());
                holder.setText(R.id.detail,infoBean.getDesc()==null?"":infoBean.getDesc());
                if (null!=infoBean.getImg()&&infoBean.getImg().length()>0){
                    Glide.with(mContext).load(infoBean.getImg()).crossFade().into((ImageView) holder.getView(R.id.img));
                }
                holder.setVisible(R.id.buofang,infoBean.getType()==3?false:true);
                break;
        }

        holder.setText(R.id.time,infoBean.getUpdated_at()==null?"":infoBean.getUpdated_at());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = holder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, holder , position);
                }
            }
        });
    }
}
