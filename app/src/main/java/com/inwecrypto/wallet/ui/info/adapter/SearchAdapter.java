package com.inwecrypto.wallet.ui.info.adapter;

import android.content.Context;

import com.inwecrypto.wallet.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2017/11/21 19:07
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class SearchAdapter extends CommonAdapter<String> {
    public SearchAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.title, null==s?"":s);
    }
}
