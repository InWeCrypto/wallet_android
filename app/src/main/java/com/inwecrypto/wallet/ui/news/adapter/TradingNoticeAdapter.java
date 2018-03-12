package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.TradingNoticeBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/22 17:19
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class TradingNoticeAdapter extends CommonAdapter<TradingNoticeBean> {
    public TradingNoticeAdapter(Context context, int layoutId, List<TradingNoticeBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TradingNoticeBean trading, int position) {

        holder.setText(R.id.time,trading.getTime());
        holder.setText(R.id.order_time,trading.getTime());
        if ("ETH".equals(trading.getFlag())){
            BigDecimal currentPrice = new BigDecimal(AppUtil.toD(trading.getMoney().replace("0x", "0")));
            currentPrice = currentPrice.divide(Constant.pEther, 4, BigDecimal.ROUND_DOWN);
            holder.setText(R.id.price,currentPrice.toPlainString());
        }else {
            holder.setText(R.id.price,trading.getMoney());
        }
        holder.setText(R.id.unit,"("+trading.getFlag()+")");
        holder.setText(R.id.to_address,trading.getTo());
        holder.setText(R.id.from_address,trading.getFrom());
        holder.setText(R.id.wallet,trading. getWallet_name());

    }
}
