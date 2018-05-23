package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.DrawRecordBean;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.wanjian.cockroach.App;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 作者：xiaoji06 on 2018/4/24 15:24
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class GetHongbaoAdapter extends CommonAdapter<DrawRecordBean> {

    public GetHongbaoAdapter(Context context, int layoutId, List<DrawRecordBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, DrawRecordBean data, int position) {
        holder.setText(R.id.address,data.getDraw_addr());
        if (data.getRedbag().getDone()==0||data.getRedbag().getDone()==1){//待开奖
            holder.setText(R.id.num,"***"+data.getRedbag().getRedbag_symbol());
            holder.setText(R.id.state,mContext.getString(R.string.daikaijiang));
        }else{//已开奖
            String price=new BigDecimal(AppUtil.toD(data.getValue().replace("0x","0"))).divide(AppUtil.decimal(data.getRedbag().getGnt_category().getDecimals()+""),4, RoundingMode.DOWN).toPlainString();
            holder.setText(R.id.num,price+data.getRedbag().getRedbag_symbol());
            holder.setText(R.id.state,mContext.getString(R.string.yikaijiang));
        }
    }
}
