package com.inwecrypto.wallet.ui.wallet.activity.neowallet;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.App;
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
    private BigDecimal decimals;
    private String amount;

    public NeoRecordAdapter(Context context,String address, int layoutId, List<NeoOderBean.ListBean> datas) {
        super(context, layoutId, datas);
        this.address=address;
    }

    public NeoRecordAdapter(Context context,String address, int layoutId, List<NeoOderBean.ListBean> datas,String decimals) {
        super(context, layoutId, datas);
        this.address=address;
        this.decimals=new BigDecimal(10).pow(Integer.parseInt(decimals));
    }

    @Override
    protected void convert(ViewHolder holder, NeoOderBean.ListBean neoOderBean, int position) {
        amount="0.0000";
        if (null!=decimals){
            try{
                amount=new BigDecimal(neoOderBean.getValue()).divide(decimals).setScale(8,BigDecimal.ROUND_DOWN).toPlainString();
            }catch (Exception e){
                amount=new BigDecimal(AppUtil.toD(neoOderBean.getValue())).divide(decimals).setScale(8,BigDecimal.ROUND_DOWN).toPlainString();
            }
        }else {
            amount=new BigDecimal(neoOderBean.getValue()).setScale(8,BigDecimal.ROUND_DOWN).toPlainString();
        }
        if (neoOderBean.getFrom().toLowerCase().equals(neoOderBean.getTo().toLowerCase())){
            Glide.with(mContext).load(R.mipmap.zizhuanxxhdpi).crossFade().into((ImageView) holder.getView(R.id.img));
            holder.setText(R.id.price,amount);
            //holder.setTextColor(R.id.price, Color.parseColor("#000000"));
            holder.setTextColor(R.id.hit,Color.parseColor("#333333"));
        }else if (neoOderBean.getFrom().toLowerCase().equals(address.toLowerCase())){
            Glide.with(mContext).load(R.mipmap.zhuanchuxxhdpi).crossFade().into((ImageView) holder.getView(R.id.img));
            holder.setText(R.id.price,"-"+amount);
            //holder.setTextColor(R.id.hit,Color.parseColor("#737373"));
            holder.setTextColor(R.id.price, Color.parseColor("#F10101"));
        }else {
            Glide.with(mContext).load(R.mipmap.zhuanruxxhdpi).crossFade().into((ImageView) holder.getView(R.id.img));
            holder.setText(R.id.price,"+"+amount);
            //holder.setTextColor(R.id.price, Color.parseColor("#000000"));
            holder.setTextColor(R.id.hit,Color.parseColor("#333333"));
        }

        if (neoOderBean.getIs_token()==1){
            Glide.with(mContext).load(R.mipmap.heyuezhuanchuxxhdpi).crossFade().into((ImageView) holder.getView(R.id.img));
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
