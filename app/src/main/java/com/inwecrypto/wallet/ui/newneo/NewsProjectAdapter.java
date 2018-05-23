package com.inwecrypto.wallet.ui.newneo;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ProjectDetaileBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import javax.microedition.khronos.opengles.GL;

/**
 * 作者：xiaoji06 on 2018/3/14 17:38
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NewsProjectAdapter extends CommonAdapter<ProjectDetaileBean> {
    public NewsProjectAdapter(Context context, int layoutId, List<ProjectDetaileBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ProjectDetaileBean projectDetaileBean, int position) {
        holder.setText(R.id.name,projectDetaileBean.getName()+" ("+projectDetaileBean.getLong_name()+")");
        if (null!=projectDetaileBean.getImg()){
            Glide.with(mContext).load(projectDetaileBean.getImg())
                    .crossFade()
                    .into((ImageView) holder.getView(R.id.img));
        }
    }
}
