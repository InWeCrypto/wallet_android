package com.inwecrypto.wallet.ui.newneo;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 作者：xiaoji06 on 2018/1/9 17:01
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NewNeoMainTokenAdapter extends CommonAdapter<TokenBean.ListBean> {
    public NewNeoMainTokenAdapter(Context context, int layoutId, List<TokenBean.ListBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TokenBean.ListBean listBean, int position) {
        try{
            Glide.with(mContext).load(Integer.parseInt(listBean.getGnt_category().getIcon())).crossFade().into((ImageView) holder.getView(R.id.img));
        }catch (Exception e){
            if (null!=listBean.getGnt_category().getIcon()) {
                Glide.with(mContext).load(listBean.getGnt_category().getIcon()).crossFade().into((ImageView) holder.getView(R.id.img));
            }else {
                Glide.with(mContext).load(R.drawable.trans_bg).crossFade().into((ImageView) holder.getView(R.id.img));
            }
        }

        holder.setText(R.id.name,listBean.getName());
        holder.setText(R.id.amount,new BigDecimal(listBean.getBalance()).setScale(4,BigDecimal.ROUND_DOWN).toPlainString());
        if (listBean.getType()==1){
            holder.setText(R.id.type,"(eth)");
        }else if (listBean.getType()==2){
            holder.setText(R.id.type,"(neo)");
        }else {
            holder.setText(R.id.type,"");
        }
    }
}
