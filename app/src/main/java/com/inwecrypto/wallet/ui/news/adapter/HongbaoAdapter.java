package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.SendHongbaoBean;
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

public class HongbaoAdapter extends CommonAdapter<SendHongbaoBean> {

    private int currentBlock;

    public HongbaoAdapter(Context context, int layoutId, List<SendHongbaoBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, SendHongbaoBean data, int position) {
        holder.setText(R.id.address,data.getRedbag_addr());
        holder.setText(R.id.num,new BigDecimal(data.getRedbag()).setScale(4, RoundingMode.DOWN).toPlainString()+data.getRedbag_symbol());
        switch (data.getStatus()){
            case 1:
                holder.setText(R.id.state,mContext.getString(R.string.wancheng));
                holder.getView(R.id.state).setVisibility(View.VISIBLE);
                holder.getView(R.id.statell).setVisibility(View.GONE);
                break;
            case 2:
                holder.getView(R.id.state).setVisibility(View.VISIBLE);
                holder.getView(R.id.statell).setVisibility(View.GONE);
                if (0!=data.getAuth_block()&&(currentBlock-data.getAuth_block())>=12){
                    holder.setText(R.id.state,mContext.getString(R.string.lijindabaochenggong));
                } else {
                    holder.setText(R.id.state,mContext.getString(R.string.lijindabaozhong));
                }
                break;
            case -2:
                holder.setText(R.id.state,mContext.getString(R.string.lijindabaoshibai));
                holder.getView(R.id.state).setVisibility(View.VISIBLE);
                holder.getView(R.id.statell).setVisibility(View.GONE);
                break;
            case -202:
                holder.setText(R.id.state,mContext.getString(R.string.zhunbeilijindabao));
                holder.getView(R.id.state).setVisibility(View.VISIBLE);
                holder.getView(R.id.statell).setVisibility(View.GONE);
                break;
            case 3:
                holder.getView(R.id.state).setVisibility(View.VISIBLE);
                holder.getView(R.id.statell).setVisibility(View.GONE);
                if (0!=data.getRedbag_block()&&(currentBlock-data.getRedbag_block())>=12){
                    holder.setText(R.id.state,mContext.getString(R.string.hongbaochuangjianchenggong));
                } else {
                    holder.setText(R.id.state,mContext.getString(R.string.hongbaochaungjianzhong));
                }
                break;
            case -3:
                holder.setText(R.id.state,mContext.getString(R.string.hongbaochuangjianshibai));
                holder.getView(R.id.state).setVisibility(View.VISIBLE);
                holder.getView(R.id.statell).setVisibility(View.GONE);
                break;
            case -303:
                holder.setText(R.id.state,mContext.getString(R.string.zhunbeichuangjianhongbao));
                holder.getView(R.id.state).setVisibility(View.VISIBLE);
                holder.getView(R.id.statell).setVisibility(View.GONE);
                break;
            case 4:
                holder.getView(R.id.state).setVisibility(View.GONE);
                holder.getView(R.id.statell).setVisibility(View.VISIBLE);
                holder.setText(R.id.p_num,data.getDraw_redbag_number()+"/"+data.getRedbag_number());
                int pec= (int) (data.getDraw_redbag_number()*1.0f/data.getRedbag_number()*1.0f*100f);
                ((ProgressBar)holder.getView(R.id.progess)).setProgress(pec);
                break;
        }
    }

    public int getCurrentBlock() {
        return currentBlock;
    }

    public void setCurrentBlock(int currentBlock) {
        this.currentBlock = currentBlock;
    }
}
