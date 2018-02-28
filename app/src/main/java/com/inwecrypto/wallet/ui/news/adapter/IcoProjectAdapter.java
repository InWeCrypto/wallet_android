package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.graphics.Color;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.TradingProjectDetaileBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/12 14:46
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class IcoProjectAdapter extends CommonAdapter<TradingProjectDetaileBean.CategoryStructureBean> {

    public IcoProjectAdapter(Context context, int layoutId, List<TradingProjectDetaileBean.CategoryStructureBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TradingProjectDetaileBean.CategoryStructureBean categoryStructureBean, int position) {
        holder.getView(R.id.view).setBackgroundColor(Color.parseColor(categoryStructureBean.getColor_value()));
        holder.setText(R.id.txt,categoryStructureBean.getPercentage()+"% "+categoryStructureBean.getDesc());
    }
}
