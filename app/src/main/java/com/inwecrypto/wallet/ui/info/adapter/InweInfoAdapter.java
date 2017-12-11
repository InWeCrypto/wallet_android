package com.inwecrypto.wallet.ui.info.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.NewsBean;
import com.inwecrypto.wallet.common.widget.MultiItemCommonAdapter;
import com.inwecrypto.wallet.common.widget.MultiItemTypeSupport;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2017/11/14 15:24
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class InweInfoAdapter extends MultiItemCommonAdapter<NewsBean> {


    public InweInfoAdapter(Context context, List<NewsBean> datas, MultiItemTypeSupport<NewsBean> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);
    }

    @Override
    protected void convert(ViewHolder holder, NewsBean infoBean, int position) {
        switch (infoBean.getType()){
            case 1:
                holder.setText(R.id.title,mContext.getString(R.string.kuaixun)+(infoBean.getTitle()==null?"":infoBean.getTitle()));
                break;
            case 2:
            case 3:
                holder.setText(R.id.title,infoBean.getTitle()==null?"":infoBean.getTitle());
                if (infoBean.getType()==3){
                    holder.setText(R.id.company,infoBean.getAuthor()==null?"":infoBean.getAuthor());
                }else {
                    holder.setText(R.id.company,infoBean.getDesc()==null?"":infoBean.getDesc());
                }

                if (null!=infoBean.getImg()&&infoBean.getImg().length()>0){
                    Glide.with(mContext).load(infoBean.getImg()).crossFade().into((ImageView) holder.getView(R.id.img));
                }
                holder.setVisible(R.id.buofang,infoBean.getType()==3?false:true);
                break;
        }

        holder.setText(R.id.shijian,infoBean.getUpdated_at()==null?"":infoBean.getUpdated_at());
    }
}
