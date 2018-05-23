package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.HongbaoRecordDetaileBean;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 作者：xiaoji06 on 2018/5/7 11:38
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class HongbaoReciveDetaileAdapter extends CommonAdapter<HongbaoRecordDetaileBean.DrawsBean> {
    private String decimal="18";
    private String symble;
    private boolean isOpen;
    public HongbaoReciveDetaileAdapter(Context context, int layoutId, List<HongbaoRecordDetaileBean.DrawsBean> datas,String symble,boolean isOpen) {
        super(context, layoutId, datas);
        this.symble=symble;
        this.isOpen=isOpen;
    }

    @Override
    protected void convert(ViewHolder holder, HongbaoRecordDetaileBean.DrawsBean drawsBean, int position) {
        holder.setText(R.id.address,drawsBean.getDraw_addr());
        if (!isOpen){
            holder.setText(R.id.num,"***" + symble);
        }else {
            if (null==drawsBean.getValue()){
                holder.setText(R.id.num,"***" + symble);
            }else {
                holder.setText(R.id.num,new BigDecimal(AppUtil.toD(drawsBean.getValue().replace("0x","0"))).divide(AppUtil.decimal(decimal),4, RoundingMode.DOWN).toPlainString() + symble);
            }
        }
        holder.setText(R.id.time,AppUtil.getGTime(drawsBean.getCreated_at()));
    }

    public void setIsOpen(boolean isOpen){
        this.isOpen=isOpen;
    }

    public void setDecimal(String decimal){
        this.decimal=decimal;
    }
}
