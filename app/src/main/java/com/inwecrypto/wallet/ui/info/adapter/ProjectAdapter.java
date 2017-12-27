package com.inwecrypto.wallet.ui.info.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ProjectBean;
import com.inwecrypto.wallet.bean.ProjectShowBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.widget.MultiItemCommonAdapter;
import com.inwecrypto.wallet.common.widget.MultiItemTypeSupport;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.List;

/**
 * 作者：xiaoji06 on 2017/11/14 15:24
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectAdapter extends MultiItemCommonAdapter<ProjectShowBean> {

    private int width1;
    private int width2;
    private LinearLayout.LayoutParams params1;
    private RecyclerView.LayoutParams params2;

    public ProjectAdapter(Context context, List<ProjectShowBean> datas, MultiItemTypeSupport<ProjectShowBean> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);

        width2 = (ScreenUtils.getScreenWidth(mContext)-DensityUtil.dip2px(mContext,13))/2;
        width1 = (width2 - DensityUtil.dip2px(mContext, 3)) / 2;
        params1 = new LinearLayout.LayoutParams(width1+(DensityUtil.dip2px(mContext, 1)), width1);
        params2 = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, width2);

    }

    @Override
    protected void convert(ViewHolder holder, ProjectShowBean projectBean, int position) {

        switch (getItemViewType(position - 1)) {
            case 1:
                FrameLayout card1 = holder.getView(R.id.small_1);
                card1.setLayoutParams(params1);
                //设置数据及相关信息
                setInfo(card1, position - 1, projectBean.getProject1(), 1);

                FrameLayout card2 = holder.getView(R.id.small_2);
                card2.setLayoutParams(params1);
                //设置数据及相关信息
                setInfo(card2, position - 1, projectBean.getProject2(), 2);

                FrameLayout card3 = holder.getView(R.id.small_3);
                card3.setLayoutParams(params1);
                //设置数据及相关信息
                setInfo(card3, position - 1, projectBean.getProject3(), 3);

                FrameLayout card4 = holder.getView(R.id.small_4);
                card4.setLayoutParams(params1);
                //设置数据及相关信息
                setInfo(card4, position - 1, projectBean.getProject4(), 4);

                break;
            case 2:
                FrameLayout bg3 = holder.getView(R.id.bg);
                bg3.setLayoutParams(params2);

                //设置数据及相关信息
                setInfo(bg3, position - 1, projectBean.getProject1(), 1);

                break;
        }
    }

    private void setInfo(FrameLayout view, final int position, final ProjectBean projectBean, final int index) {

        if (null == projectBean) {
            view.setVisibility(View.INVISIBLE);
            return;
        }else {
            view.setVisibility(View.VISIBLE);
        }

        //view.setBackgroundColor(Color.parseColor(projectBean.getColor()));
        TextView charge = (TextView) view.findViewById(R.id.charge);
        TextView price = (TextView) view.findViewById(R.id.price);
        ImageView img = (ImageView) view.findViewById(R.id.img);
        TextView name = (TextView) view.findViewById(R.id.name);

        if (null != projectBean.getImg()) {
            Glide.with(mContext).load(projectBean.getImg()).crossFade().into(img);
        }
        name.setText(projectBean.getName());

        switch (projectBean.getType()) {
            case 5:
//                charge.setText((null==projectBean.getZhangfu()?"N/N%":(projectBean.getZhangfu()+"%")));
//                price.setText((null==projectBean.getCurrentPrie()?"$N/A":("$"+new BigDecimal(projectBean.getCurrentPrie()).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString())));
                charge.setText("");
                price.setText("");
                break;
            case 6:
                charge.setText("");
                price.setText("");
                break;
            case 7:
                charge.setText("");
                price.setText("");
                break;
            case 8:
                charge.setText("");
                price.setText("");
                break;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_PROJECT,position,index));
            }
        });
    }
}
