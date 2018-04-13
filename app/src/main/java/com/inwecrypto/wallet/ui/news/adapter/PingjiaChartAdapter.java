package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ProjectDetailAnalysisBean;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/4/11 18:37
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class PingjiaChartAdapter extends CommonAdapter<ProjectDetailAnalysisBean> {

    private int max;
    private int width;
    private int height;

    public PingjiaChartAdapter(Context context, int layoutId, List<ProjectDetailAnalysisBean> datas) {
        super(context, layoutId, datas);
        height= DensityUtil.dip2px(mContext,70);
    }

    public void setParams(int max,int width){
        this.max=max;
        this.width=width;
    }

    @Override
    protected void convert(ViewHolder holder, ProjectDetailAnalysisBean data, int position) {
        ViewGroup.LayoutParams params = holder.getView(R.id.linell).getLayoutParams();
        params.width=(int) (width/12.0);
        holder.getView(R.id.linell).setLayoutParams(params);

        LinearLayout.LayoutParams upParams = (LinearLayout.LayoutParams) holder.getView(R.id.up).getLayoutParams();
        upParams.height= (int) (height*(data.getLike()/(max*1.0f)));
        holder.getView(R.id.up).setLayoutParams(upParams);

        LinearLayout.LayoutParams downParams = (LinearLayout.LayoutParams) holder.getView(R.id.down).getLayoutParams();
        downParams.height= (int) (height*(data.getVery_dissatisfied()/(max*1.0f)));
        holder.getView(R.id.down).setLayoutParams(downParams);

    }
}
