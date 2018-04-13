package com.inwecrypto.wallet.ui.wallet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.util.List;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.OrderBean;
import com.inwecrypto.wallet.ui.wallet.activity.TokenWalletActivity;

/**
 * Created by Administrator on 2017/8/10.
 * 功能描述：
 * 版本：@version
 */

public class RecordAdapter extends CommonAdapter<OrderBean> {

    private String address;
    private TokenWalletActivity activity;
    private BigDecimal pEther = new BigDecimal("1000000000000000000");
    private String unit;
    private boolean isEth;
    private BigDecimal decimalUnit;

    public RecordAdapter(Context context, int layoutId, List<OrderBean> datas,String address,String unit,String decimal) {
        super(context, layoutId, datas);
        this.address=address;
        activity= (TokenWalletActivity) context;
        this.unit=unit;
        if ("ether".equals(unit)){
            isEth=true;
        }else {
            decimalUnit=AppUtil.decimal(decimal);
        }
    }

    @Override
    protected void convert(ViewHolder holder, OrderBean orderBean, int position) {
        BigDecimal currentPrice = new BigDecimal(orderBean.getBlock_number());
        int current= (int) (activity.currentBlock-currentPrice.doubleValue())+1;
        if (current<0){
                current=0;
        }
        if (orderBean.getPay_address().toLowerCase().equals(address.toLowerCase())){
            if ("".equals(orderBean.getConfirm_at())&&current>=activity.minBlock){//交易失败
                holder.setText(R.id.hit,mContext.getString(R.string.jiaoyishibai));
                holder.setVisible(R.id.progess,false);
                orderBean.setStatus(0);
            }else if ("".equals(orderBean.getConfirm_at())&&current<activity.minBlock){//准备打包
                holder.setText(R.id.hit,mContext.getString(R.string.zhunbeidabao));
                holder.setVisible(R.id.progess,true);
                ProgressBar bar=holder.getView(R.id.progess);
                bar.setProgress(0);
                orderBean.setStatus(1);
            }else if ((!"".equals(orderBean.getConfirm_at()))&&current<activity.minBlock){//打包中
                holder.setVisible(R.id.progess,true);
                holder.setText(R.id.hit,mContext.getString(R.string.yijingqueren)+current+"/"+activity.minBlock);
                ProgressBar bar=holder.getView(R.id.progess);
                bar.setProgress((int) (current*1.0f/activity.minBlock*100.f));
                if (current>=activity.minBlock){
                    holder.setText(R.id.hit,mContext.getString(R.string.jiaoyichenggong));
                    holder.setVisible(R.id.progess,false);
                }
                orderBean.setStatus(1);
            }else if ((!"".equals(orderBean.getConfirm_at()))&&current>=activity.minBlock){//交易成功
                holder.setText(R.id.hit,mContext.getString(R.string.jiaoyichenggong));
                holder.setVisible(R.id.progess,false);
                orderBean.setStatus(0);
            }
            String price="0.0000";
            if (orderBean.getFee().startsWith("0x")){
                price=AppUtil.toD(orderBean.getFee());
            }else {
                price=orderBean.getFee();
            }
            if (orderBean.getPay_address().equals(orderBean.getReceive_address())){
                holder.setText(R.id.price,new BigDecimal(price).divide((isEth?pEther:decimalUnit),4,BigDecimal.ROUND_DOWN).toPlainString()+unit);
                Glide.with(mContext).load(R.mipmap.zizhuanxxhdpi).crossFade().into((ImageView) holder.getView(R.id.img));
            }else {
                holder.setText(R.id.price,"-"+new BigDecimal(price).divide((isEth?pEther:decimalUnit),4,BigDecimal.ROUND_DOWN).toPlainString()+unit);
                Glide.with(mContext).load(R.mipmap.zhuanchuxxhdpi).crossFade().into((ImageView) holder.getView(R.id.img));
            }
            holder.setTextColor(R.id.price, Color.parseColor("#F81A1A"));
        }else {
            Glide.with(mContext).load(R.mipmap.zhuanruxxhdpi).crossFade().into((ImageView) holder.getView(R.id.img));
            if ("".equals(orderBean.getConfirm_at())&&current>=activity.minBlock){//交易失败
                holder.setText(R.id.hit,mContext.getString(R.string.jiaoyishibai));
                holder.setVisible(R.id.progess,false);
                orderBean.setStatus(0);
            }else if ("".equals(orderBean.getConfirm_at())&&current<activity.minBlock){//准备打包
                holder.setText(R.id.hit,mContext.getString(R.string.zhunbeidabao));
                holder.setVisible(R.id.progess,true);
                ProgressBar bar=holder.getView(R.id.progess);
                bar.setProgress(0);
                orderBean.setStatus(1);
            }else if ((!"".equals(orderBean.getConfirm_at()))&&current<activity.minBlock){//打包中
                holder.setVisible(R.id.progess,true);
                holder.setText(R.id.hit,mContext.getString(R.string.yijingqueren)+current+"/"+activity.minBlock);
                ProgressBar bar=holder.getView(R.id.progess);
                bar.setProgress((int) (current*1.0f/activity.minBlock*100.f));
                if (current>=activity.minBlock){
                    holder.setText(R.id.hit,mContext.getString(R.string.jiaoyichenggong));
                    holder.setVisible(R.id.progess,false);
                }
                orderBean.setStatus(1);
            }else if ((!"".equals(orderBean.getConfirm_at()))&&current>=activity.minBlock){//交易成功
                holder.setText(R.id.hit,mContext.getString(R.string.jiaoyichenggong));
                holder.setVisible(R.id.progess,false);
                orderBean.setStatus(0);
            }
            String price="0.0000";
            if (orderBean.getFee().startsWith("0x")){
                price=AppUtil.toD(orderBean.getFee());
            }else {
                price=orderBean.getFee();
            }
            holder.setText(R.id.price,"+"+new BigDecimal(price).divide((isEth?pEther:decimalUnit),4,BigDecimal.ROUND_DOWN).toPlainString()+unit);
            holder.setTextColorRes(R.id.price,R.color.c_232772);
        }
        holder.setText(R.id.order,orderBean.getTrade_no());
        holder.setText(R.id.time, orderBean.getCreated_at().contains("T")?AppUtil.getTime(orderBean.getCreated_at()):orderBean.getCreated_at());
    }
}
