package com.inwecrypto.wallet.ui.wallet.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.WalletBean;

/**
 * Created by Administrator on 2017/8/19.
 * 功能描述：
 * 版本：@version
 */

public class IcoWalletAdapter extends CommonAdapter<WalletBean> {

    public IcoWalletAdapter(Context context, int layoutId, List<WalletBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, WalletBean walletBean, int position) {
            holder.setText(R.id.name,walletBean.getName());
    }
}
