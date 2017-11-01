package com.inwecrypto.wallet.ui.me.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.UnitBean;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class UnitAdapter extends CommonAdapter<UnitBean> {

    public UnitAdapter(Context context, int layoutId, List<UnitBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, UnitBean unitBean, int position) {
        holder.setText(R.id.name,unitBean.getName());
        if (0==unitBean.getUser_unit_count()){
            Glide.with(mContext).load(R.mipmap.list_btn_default).crossFade().into((ImageView) holder.getView(R.id.select));
        }else {
            Glide.with(mContext).load(R.mipmap.list_btn_selected).crossFade().into((ImageView) holder.getView(R.id.select));
        }
    }
}
