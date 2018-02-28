package com.inwecrypto.wallet.ui.me.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.MailBean;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import me.grantland.widget.AutofitTextView;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class MailListAdapter extends CommonAdapter<MailBean> {


    public MailListAdapter(Context context, int layoutId, List<MailBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MailBean mailBean, int position) {
        holder.setText(R.id.name,mailBean.getName());
        ((AutofitTextView)holder.getView(R.id.address)).setText(mailBean.getAddress());
    }

}
