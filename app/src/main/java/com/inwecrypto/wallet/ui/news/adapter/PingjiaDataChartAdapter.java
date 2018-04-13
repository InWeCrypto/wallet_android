package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ProjectDetailAnalysisBean;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 作者：xiaoji06 on 2018/4/11 18:37
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class PingjiaDataChartAdapter extends CommonAdapter<ProjectDetailAnalysisBean> {

    private int max;
    private int width;
    private int height;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfA = new SimpleDateFormat("MM.dd");

    public PingjiaDataChartAdapter(Context context, int layoutId, List<ProjectDetailAnalysisBean> datas) {
        super(context, layoutId, datas);
        height= DensityUtil.dip2px(mContext,70);
    }

    public void setParams(int max,int width){
        this.max=max;
        this.width=width;
    }

    @Override
    protected void convert(ViewHolder holder, ProjectDetailAnalysisBean data, int position) {
        ViewGroup.LayoutParams params = holder.getView(R.id.txt).getLayoutParams();
        params.width=(int) (width/12.0);
        holder.getView(R.id.txt).setLayoutParams(params);
        try {
            holder.setText(R.id.txt,sdfA.format(sdf.parse(data.getDate_range())));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.setText(R.id.txt,"");
        }
    }
}
