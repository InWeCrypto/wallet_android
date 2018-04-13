package com.inwecrypto.wallet.ui.newneo;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.NewNeoTokenListBean;
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

public class NewNeoWalletListPopupAdapter extends CommonAdapter<NewNeoTokenListBean> {
    public NewNeoWalletListPopupAdapter(Context context, int layoutId, List<NewNeoTokenListBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, NewNeoTokenListBean listBean, int position) {
        if (listBean.getWallet().getCategory_id()==1){
            Glide.with(mContext).load(R.mipmap.project_icon_eth).crossFade().into((ImageView) holder.getView(R.id.img));
        }else {
            Glide.with(mContext).load(R.mipmap.neoxxhdpi).crossFade().into((ImageView) holder.getView(R.id.img));
        }
        holder.setText(R.id.name,listBean.getName());
        holder.setText(R.id.amount,new BigDecimal(listBean.getPrice()).setScale(4,BigDecimal.ROUND_HALF_UP).toPlainString());
    }
}
