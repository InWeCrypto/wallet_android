package com.inwecrypto.wallet.ui.wallet.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.App;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.util.List;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;

/**
 * Created by Administrator on 2017/7/15.
 * 功能描述：
 * 版本：@version
 */
//ETHPrice = ETHPrice.add(currentPrice.divide(Constant.pEther).multiply(new BigDecimal(count.getCategory().getCap().getPrice_cny())));
//        } else {

public class MainGntAdapter extends CommonAdapter<TokenBean.ListBean> {

    public MainGntAdapter(Context context, int layoutId, List<TokenBean.ListBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TokenBean.ListBean gntBean, int position) {
        if (gntBean.getName().equals("ETH")||gntBean.getName().equals("NEO")||gntBean.getName().equals("Gas")){
            Glide.with(mContext).load(Integer.parseInt(gntBean.getGnt_category().getIcon())).transform(new GlideCircleTransform(mContext)).into((ImageView) holder.getView(R.id.iv_img));
        }else {
            Glide.with(mContext).load(gntBean.getGnt_category().getIcon()).transform(new GlideCircleTransform(mContext)).into((ImageView) holder.getView(R.id.iv_img));
        }
        holder.setText(R.id.name,gntBean.getName());
        BigDecimal currentPrice = new BigDecimal(gntBean.getBalance());
        holder.setText(R.id.tv_price,currentPrice.setScale(4,BigDecimal.ROUND_DOWN).toString());
        if (null==gntBean.getGnt_category().getCap()){
            if (1== App.get().getUnit()){
                holder.setText(R.id.tv_eth_ch_price,"≈￥0.00");
            }else {
                holder.setText(R.id.tv_eth_ch_price,"≈$0.00");
            }
            return;
        }else {
            if (1== App.get().getUnit()){
                holder.setText(R.id.tv_eth_ch_price,"≈￥"+currentPrice.multiply(new BigDecimal(gntBean.getGnt_category().getCap().getPrice_cny())).setScale(2,BigDecimal.ROUND_DOWN).toString());
            }else {
                holder.setText(R.id.tv_eth_ch_price,"≈$"+currentPrice.multiply(new BigDecimal(gntBean.getGnt_category().getCap().getPrice_usd())).setScale(2,BigDecimal.ROUND_DOWN).toString());
            }
        }
    }
}
