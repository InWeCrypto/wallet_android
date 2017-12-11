package com.inwecrypto.wallet.ui.info.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.ProjectBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2017/11/17 10:53
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class MoreInfoAdapter extends CommonAdapter<ProjectBean.ProjectMediasBean> {
    public MoreInfoAdapter(Context context, int layoutId, List<ProjectBean.ProjectMediasBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ProjectBean.ProjectMediasBean projectMediasBean, int position) {
        if (null!=projectMediasBean.getImg()){
            Glide.with(mContext).load(projectMediasBean.getImg()).crossFade().into((ImageView) holder.getView(R.id.img));
        }
    }
}
