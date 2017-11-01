package com.inwecrypto.wallet.ui.me.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.IcoOrderBean;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class IcoOrderAdapter extends CommonAdapter<IcoOrderBean> {


    public IcoOrderAdapter(Context context, int layoutId, List<IcoOrderBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, IcoOrderBean icoOrderBean, int position) {
        holder.setText(R.id.name,icoOrderBean.getIco().getTitle());
        holder.setText(R.id.type,icoOrderBean.getIco().getCny());
        holder.setText(R.id.time,icoOrderBean.getCreated_at());
        holder.setText(R.id.price,icoOrderBean.getFee());
    }

}
