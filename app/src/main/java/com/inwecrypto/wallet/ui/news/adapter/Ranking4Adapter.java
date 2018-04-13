package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.Rank3Bean;
import com.inwecrypto.wallet.bean.Rank4Bean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.kelin.scrollablepanel.library.PanelAdapter;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xiaoji06 on 2018/4/2 14:39
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class Ranking4Adapter extends CommonAdapter<Rank4Bean>{
    public Ranking4Adapter(Context context, int layoutId, List<Rank4Bean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Rank4Bean rank4Bean, final int position) {
        holder.getView(R.id.namell).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_PAIXU4,position));
            }
        });
        holder.setText(R.id.no,(position+1)+"");
        holder.setText(R.id.name,rank4Bean.getName());
        holder.setText(R.id.long_name,"("+rank4Bean.getLong_name()+")");
        holder.setText(R.id.detaile,rank4Bean.getIndustry());
        holder.setText(R.id.pingfen,new BigDecimal(null==rank4Bean.getScore()?"0":rank4Bean.getScore()).setScale(1, RoundingMode.HALF_UP).toPlainString()+mContext.getString(R.string.fenshu));
        holder.setText(R.id.erdu,"#"+rank4Bean.getRank());
        Glide.with(mContext).load(rank4Bean.getImg()).crossFade().into((ImageView) holder.getView(R.id.img));
    }
}
