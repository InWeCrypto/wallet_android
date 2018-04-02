package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ArticleDetaileBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.widget.MultiItemCommonAdapter;
import com.inwecrypto.wallet.common.widget.MultiItemTypeSupport;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/3/5 14:31
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class InwehotNewsAdapter extends MultiItemCommonAdapter<ArticleDetaileBean> {
    private LinearLayout.LayoutParams params;
    protected MultiItemTypeSupport<ArticleDetaileBean> mMultiItemTypeSupport;

    public InwehotNewsAdapter(Context context, List<ArticleDetaileBean> datas, MultiItemTypeSupport<ArticleDetaileBean> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);
        int hight= (int) ((ScreenUtils.getScreenWidth(mContext)- DensityUtil.dip2px(mContext,30))/690.0*333.0);
        params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,hight);
        this.mMultiItemTypeSupport=multiItemTypeSupport;
    }

    @Override
    public int getItemViewType(int position) {
        return mMultiItemTypeSupport.getItemViewType(position, mDatas.get(position));
    }

    @Override
    protected void convert(ViewHolder holder, ArticleDetaileBean articleDetaileBean, final int position) {
        if (getItemViewType(position)==1){
            holder.setText(R.id.time, AppUtil.getGTime(articleDetaileBean.getCreated_at()));
            holder.setText(R.id.title,null==articleDetaileBean.getTitle()?"":articleDetaileBean.getTitle());
            holder.setText(R.id.content,articleDetaileBean.getDesc());
        }else if (getItemViewType(position)==2){
            holder.setText(R.id.time, AppUtil.getGTime(articleDetaileBean.getCreated_at()));
            holder.setText(R.id.from,articleDetaileBean.getAuthor());
            holder.setText(R.id.order_time, AppUtil.getGTime(articleDetaileBean.getCreated_at()));
            holder.setText(R.id.content,articleDetaileBean.getTitle());
        }else{
                holder.setText(R.id.time, AppUtil.getGTime(articleDetaileBean.getCreated_at()));
                holder.setText(R.id.title,null==articleDetaileBean.getTitle()?"":articleDetaileBean.getTitle());

                holder.getView(R.id.img).setLayoutParams(params);
                if (null!=articleDetaileBean.getImg()){
                    Glide.with(mContext)
                            .load(articleDetaileBean.getImg())
                            .placeholder(R.mipmap.zhanweitu_ico)
                            .crossFade()
                            .into((ImageView) holder.getView(R.id.img));
                }else {
                    Glide.with(mContext)
                            .load(R.mipmap.zhanweitu_ico)
                            .crossFade()
                            .into((ImageView) holder.getView(R.id.img));
                }
        }

        holder.getView(R.id.card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_INWE_HOT_CLICK,position));
            }
        });
    }

}
