package com.inwecrypto.wallet.ui.info.adapter;

import android.content.Context;
import android.text.Html;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.IcoBean;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2017/11/15 13:31
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class IcoAdapter extends CommonAdapter<IcoBean> {

    private LinearLayout.LayoutParams params;

    public IcoAdapter(Context context, int layoutId, List<IcoBean> datas) {
        super(context, layoutId, datas);
        int width= ScreenUtils.getScreenWidth(mContext)- DensityUtil.dip2px(mContext,30);
        int hight= (int) (width/690.0*406);
        params=new LinearLayout.LayoutParams(width,hight);
    }

    @Override
    protected void convert(ViewHolder holder, IcoBean icoBean, int position) {

        ImageView img=holder.getView(R.id.img);
        img.setLayoutParams(params);

        if (null!=icoBean.getImg()){
            Glide.with(mContext).load(icoBean.getImg()).crossFade().into((ImageView) holder.getView(R.id.img));
        }

        ((TextView)holder.getView(R.id.title)).setText(Html.fromHtml("<u>"+null==icoBean.getTitle()?"":icoBean.getTitle()+"</u>"));
        holder.setText(R.id.state,null==icoBean.getAssess_status()?"":icoBean.getAssess_status());
        holder.setText(R.id.web,null==icoBean.getWebsite()?"":icoBean.getWebsite());
        holder.setText(R.id.time,null==icoBean.getUpdated_at()?"":icoBean.getUpdated_at());

        if(position==(mDatas.size()-1)){
            holder.setVisible(R.id.line,false);
        }else {
            holder.setVisible(R.id.line,true);
        }
    }
}
