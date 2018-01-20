package com.inwecrypto.wallet.ui.wallet.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.WalletBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/8/9.
 * 功能描述：
 * 版本：@version
 */

public class NeoWalletListAdapter extends CommonAdapter<WalletBean> {

    public NeoWalletListAdapter(Context context, int layoutId, List<WalletBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, WalletBean walletBean, int position) {
        Glide.with(mContext).load(R.mipmap.neoxxhdpi).crossFade().into((ImageView) holder.getView(R.id.img));
        holder.setText(R.id.name,walletBean.getName());
    }
}
