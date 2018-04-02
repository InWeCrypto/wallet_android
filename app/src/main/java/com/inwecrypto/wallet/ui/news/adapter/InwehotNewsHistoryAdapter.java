package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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

public class InwehotNewsHistoryAdapter extends MultiItemCommonAdapter<ArticleDetaileBean> {
    protected MultiItemTypeSupport<ArticleDetaileBean> mMultiItemTypeSupport;
    private int type;

    public InwehotNewsHistoryAdapter(Context context, List<ArticleDetaileBean> datas,int type, MultiItemTypeSupport<ArticleDetaileBean> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);
        this.mMultiItemTypeSupport=multiItemTypeSupport;
        this.type=type;
    }

    @Override
    public int getItemViewType(int position) {
        return mMultiItemTypeSupport.getItemViewType(position, mDatas.get(position));
    }

    @Override
    protected void convert(ViewHolder holder, ArticleDetaileBean articleDetaileBean, final int position) {
        if (getItemViewType(position)==1){
            holder.setText(R.id.time, AppUtil.getGTime(articleDetaileBean.getCreated_at()));
            holder.setText(R.id.title,articleDetaileBean.getTitle());
        }else {
            holder.setText(R.id.time, AppUtil.getGTime(articleDetaileBean.getCreated_at()));
            holder.setText(R.id.title,articleDetaileBean.getTitle());
            if (null!=articleDetaileBean.getImg()){
                Glide.with(mContext)
                        .load(articleDetaileBean.getImg())
                        .crossFade()
                        .error(R.mipmap.zhanweitu_ico)
                        .into((ImageView) holder.getView(R.id.img));
            }else {
                Glide.with(mContext)
                        .load(R.mipmap.zhanweitu_ico)
                        .crossFade()
                        .into((ImageView) holder.getView(R.id.img));
            }
        }

        if (articleDetaileBean.isIs_sole()){
            holder.getView(R.id.yuanchuang).setVisibility(View.VISIBLE);
        }else {
            holder.getView(R.id.yuanchuang).setVisibility(View.INVISIBLE);
        }

        holder.getView(R.id.card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_INWE_HOT_HISTORY_CLICK,type,position));
            }
        });
    }

}
