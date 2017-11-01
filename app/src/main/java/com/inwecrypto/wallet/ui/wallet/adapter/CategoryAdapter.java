package com.inwecrypto.wallet.ui.wallet.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.CategoryBean;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class CategoryAdapter extends CommonAdapter<CategoryBean> {

    public CategoryAdapter(Context context, int layoutId, List<CategoryBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, CategoryBean categoryBean, int position) {
        if (null!=categoryBean.getIcon()){
            Glide.with(mContext).load(categoryBean.getIcon()).crossFade().into((ImageView) holder.getView(R.id.img));
        }
        holder.setText(R.id.name,categoryBean.getName());
        if (!categoryBean.isSelect()){
            Glide.with(mContext).load(R.mipmap.list_btn_default).crossFade().into((ImageView) holder.getView(R.id.select));
        }else {
            Glide.with(mContext).load(R.mipmap.list_btn_selected).crossFade().into((ImageView) holder.getView(R.id.select));
        }
    }
}
