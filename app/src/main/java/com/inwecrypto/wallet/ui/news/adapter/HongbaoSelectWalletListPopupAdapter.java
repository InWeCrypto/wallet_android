package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.WalletBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/4/25 15:38
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class HongbaoSelectWalletListPopupAdapter extends CommonAdapter<WalletBean> {
    public HongbaoSelectWalletListPopupAdapter(Context context, int layoutId, List<WalletBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, WalletBean walletBean, int position) {
        holder.setText(R.id.name,walletBean.getName());
        holder.setText(R.id.address,walletBean.getAddress());
        if (walletBean.isSelect()){
            ((ImageView)holder.getView(R.id.select)).setImageResource(R.mipmap.red_type_select);
        }else {
            ((ImageView)holder.getView(R.id.select)).setImageResource(R.mipmap.red_type_unselect);
        }
    }
}
