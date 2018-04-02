package com.inwecrypto.wallet.ui.news.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ProjectDetaileBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/8 15:21
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectAdatpter extends CommonAdapter<ProjectDetaileBean> {
    int type;
    public ProjectAdatpter(Context context, int layoutId, List<ProjectDetaileBean> datas,int type) {
        super(context, layoutId, datas);
        this.type=type;
    }

    @Override
    protected void convert(ViewHolder holder, ProjectDetaileBean projectDetaileBean, int position) {
        holder.getView(R.id.munll).setVisibility(View.INVISIBLE);
        if (type==1){
            holder.getView(R.id.projectll).setVisibility(View.VISIBLE);
        }else {
            holder.getView(R.id.projectll).setVisibility(View.INVISIBLE);
        }

        Glide.with(mContext).load(null==projectDetaileBean.getImg()?"":projectDetaileBean.getImg()).priority(Priority.LOW).crossFade().into((ImageView) holder.getView(R.id.img));
        holder.setText(R.id.title,(null==projectDetaileBean.getName()?"":projectDetaileBean.getName())
                +" ("+(null==projectDetaileBean.getLong_name()?"":projectDetaileBean.getLong_name())+")");
        if (null!=projectDetaileBean.getLast_article()){
            holder.setText(R.id.news,projectDetaileBean.getLast_article().getTitle());
            holder.setText(R.id.time,projectDetaileBean.getLast_article().getCreated_at());
        }else {
            holder.setText(R.id.news,"");
            holder.setText(R.id.time,"");
        }
        holder.setText(R.id.price,null==projectDetaileBean.getPrice()?((App.get().getUnit()==1?"¥":"$")+"0.00"):((App.get().getUnit()==1?"¥":"$")+new BigDecimal(projectDetaileBean.getPrice()).setScale(2, BigDecimal.ROUND_DOWN).toPlainString()));

        if (projectDetaileBean.isHasMarket()){
            holder.getView(R.id.charge).setVisibility(View.VISIBLE);
            holder.getView(R.id.price).setVisibility(View.VISIBLE);
            if (null!=projectDetaileBean.getCharge()){
                if (!projectDetaileBean.getCharge().contains("-")){
                    holder.setText(R.id.charge,null==projectDetaileBean.getCharge()?"+0.00%":("+"+projectDetaileBean.getCharge()+"%"));
                    holder.getView(R.id.charge).setBackgroundResource(R.drawable.project_up_bg);
                }else {
                    holder.setText(R.id.charge,null==projectDetaileBean.getCharge()?"-0.00%":(projectDetaileBean.getCharge()+"%"));
                    holder.getView(R.id.charge).setBackgroundResource(R.drawable.project_down_bg);
                }
            }else {
                holder.setText(R.id.charge,"+0.00%");
                holder.getView(R.id.charge).setBackgroundResource(R.drawable.project_up_bg);
            }
        }else {
            holder.getView(R.id.charge).setVisibility(View.INVISIBLE);
            holder.getView(R.id.price).setVisibility(View.INVISIBLE);
        }

    }
}
