package com.inwecrypto.wallet.ui.wallet.activity.neowallet;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.NeoOderBean;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/8/10.
 * 功能描述：
 * 版本：@version
 */

public class NeoRecordAdapter extends CommonAdapter<NeoOderBean.ListBean> {

    private String address;

    public NeoRecordAdapter(Context context,String address, int layoutId, List<NeoOderBean.ListBean> datas) {
        super(context, layoutId, datas);
        this.address=address;
    }

    @Override
    protected void convert(ViewHolder holder, NeoOderBean.ListBean neoOderBean, int position) {
        if (neoOderBean.getFrom().equals(neoOderBean.getTo())){
            Glide.with(mContext).load(R.mipmap.icon_toself).crossFade().into((ImageView) holder.getView(R.id.img));
            holder.setText(R.id.price,new BigDecimal(neoOderBean.getValue()).setScale(8,BigDecimal.ROUND_HALF_UP).toPlainString());
            holder.setTextColor(R.id.price, Color.parseColor("#000000"));
            holder.setTextColor(R.id.hit,Color.parseColor("#333333"));
        }else if (neoOderBean.getFrom().equals(address)){
            Glide.with(mContext).load(R.mipmap.zhuanchu).crossFade().into((ImageView) holder.getView(R.id.img));
            holder.setText(R.id.price,"-"+new BigDecimal(neoOderBean.getValue()).setScale(8,BigDecimal.ROUND_HALF_UP).toPlainString());
            holder.setTextColor(R.id.hit,Color.parseColor("#737373"));
            holder.setTextColor(R.id.price, Color.parseColor("#F10101"));
        }else {
            Glide.with(mContext).load(R.mipmap.zhuanru).crossFade().into((ImageView) holder.getView(R.id.img));
            holder.setText(R.id.price,"+"+new BigDecimal(neoOderBean.getValue()).setScale(8,BigDecimal.ROUND_HALF_UP).toPlainString());
            holder.setTextColor(R.id.price, Color.parseColor("#000000"));
            holder.setTextColor(R.id.hit,Color.parseColor("#333333"));
        }
        if (null==neoOderBean.getConfirmTime()||neoOderBean.getConfirmTime().equals("")) {//确认中
            holder.setText(R.id.hit,mContext.getString(R.string.querenzhong));
        }else {
            holder.setText(R.id.hit,mContext.getString(R.string.jiaoyichenggong));
        }
        holder.setText(R.id.time,"".equals(neoOderBean.getCreateTime())?"": AppUtil.getTime(neoOderBean.getCreateTime()));
        holder.setText(R.id.order,neoOderBean.getTx());
    }

}
