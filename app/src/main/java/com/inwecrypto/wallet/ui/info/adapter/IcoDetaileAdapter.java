package com.inwecrypto.wallet.ui.info.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ProjectBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2017/11/17 16:52
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class IcoDetaileAdapter extends CommonAdapter<ProjectBean.IcoDetailBean> {
    public IcoDetaileAdapter(Context context, int layoutId, List<ProjectBean.IcoDetailBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ProjectBean.IcoDetailBean icoDetailBean, int position) {
        if (position==0){
            ((TextView)holder.getView(R.id.key)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            ((TextView)holder.getView(R.id.key)).setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
            ((TextView)holder.getView(R.id.value)).setTextSize(TypedValue.COMPLEX_UNIT_SP,10);

        }else {
            ((TextView)holder.getView(R.id.key)).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            ((TextView)holder.getView(R.id.key)).setTextSize(TypedValue.COMPLEX_UNIT_SP,7);
            ((TextView)holder.getView(R.id.value)).setTextSize(TypedValue.COMPLEX_UNIT_SP,7);
        }

        holder.setText(R.id.key,icoDetailBean.getName());
        holder.setText(R.id.value,icoDetailBean.getDesc());
    }
}
