package com.inwecrypto.wallet.ui.newneo;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by Administrator on 2017/7/15.
 * 功能描述：
 * 版本：@version
 */

public class NewNeoGntAdapter extends CommonAdapter<TokenBean.ListBean> {

    private BigDecimal pEther= new BigDecimal("1000000000000000000");

    public NewNeoGntAdapter(Context context, int layoutId, List<TokenBean.ListBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TokenBean.ListBean gntBean, int position) {
        holder.setText(R.id.name,gntBean.getName());
        BigInteger price=new BigInteger(AppUtil.reverseArray(gntBean.getBalance()));
        BigDecimal currentPrice = new BigDecimal(price).divide(new BigDecimal(10).pow(Integer.parseInt(gntBean.getDecimals()==null?"0":gntBean.getDecimals())));
        Glide.with(mContext).load(gntBean.getGnt_category().getIcon()).crossFade().into((ImageView) holder.getView(R.id.iv_img));
        holder.setText(R.id.tv_price,currentPrice.setScale(8,BigDecimal.ROUND_DOWN).toPlainString());
        boolean isSee=App.get().getSp().getBoolean(Constant.MAIN_SEE,true);
        if (isSee){
            if (null!=gntBean.getGnt_category().getCap()){
                if (1== App.get().getUnit()){
                    holder.setText(R.id.tv_eth_ch_price,"￥"+currentPrice.multiply(new BigDecimal(gntBean.getGnt_category().getCap().getPrice_cny())).setScale(2,BigDecimal.ROUND_DOWN).toPlainString());
                }else {
                    holder.setText(R.id.tv_eth_ch_price,"$"+currentPrice.multiply(new BigDecimal(gntBean.getGnt_category().getCap().getPrice_usd())).setScale(2,BigDecimal.ROUND_DOWN).toPlainString());
                }
            }
            if (null==gntBean.getGnt_category().getCap()){
                if (1== App.get().getUnit()){
                    holder.setText(R.id.tv_eth_ch_price,"￥0.00");
                }else {
                    holder.setText(R.id.tv_eth_ch_price,"$0.00");
                }
            }
        }else {
            if (1== App.get().getUnit()){
                holder.setText(R.id.tv_eth_ch_price,"￥****");
            }else {
                holder.setText(R.id.tv_eth_ch_price,"$****");
            }
        }
    }
}
