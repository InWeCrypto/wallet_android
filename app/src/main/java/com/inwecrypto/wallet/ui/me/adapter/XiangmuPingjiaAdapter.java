package com.inwecrypto.wallet.ui.me.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.PingjiaBean;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.widget.RatingBar;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/4/9 11:19
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class XiangmuPingjiaAdapter extends CommonAdapter<PingjiaBean> {
    public XiangmuPingjiaAdapter(Context context, int layoutId, List<PingjiaBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, PingjiaBean pingjia, int position) {
        Glide.with(mContext)
                .load(pingjia.getUser().getImg())
                .crossFade()
                .error(R.mipmap.wode_touxiang)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.img));
        holder.setText(R.id.name,pingjia.getUser().getName());
        holder.setText(R.id.time, AppUtil.getGTime(pingjia.getCategory_comment_at()));
        ((RatingBar)holder.getView(R.id.ratingbar)).setStar(Float.parseFloat(pingjia.getScore()));
        holder.setText(R.id.fenshu,pingjia.getScore()+mContext.getString(R.string.fenshu));

        String leveStr="";
        if (Float.parseFloat(pingjia.getScore())<=1){
            leveStr=mContext.getString(R.string.henbumanyi);
        }else if (Float.parseFloat(pingjia.getScore())<=2){
            leveStr=mContext.getString(R.string.bumanyi);
        }else if (Float.parseFloat(pingjia.getScore())<=3){
            leveStr=mContext.getString(R.string.yiban);
        }else if (Float.parseFloat(pingjia.getScore())<=4){
            leveStr=mContext.getString(R.string.tuijian);
        }else if (Float.parseFloat(pingjia.getScore())<=5){
            leveStr=mContext.getString(R.string.feichangtuijian);
        }

        holder.setText(R.id.leve,leveStr);
        if (null==pingjia.getCategory_comment()||"".equals(pingjia.getCategory_comment())){
            holder.setText(R.id.content,mContext.getString(R.string.morenpingjia));
        }else {
            holder.setText(R.id.content,pingjia.getCategory_comment());
        }

        if (pingjia.getIs_category_comment()==1){
            if (pingjia.getCategory_comment_tag_name().length()==0){
                holder.getView(R.id.cantouguo).setVisibility(View.GONE);
            }else {
                holder.getView(R.id.cantouguo).setVisibility(View.VISIBLE);
                holder.setText(R.id.cantouguo,pingjia.getCategory_comment_tag_name());
            }
        }else {
            holder.getView(R.id.cantouguo).setVisibility(View.GONE);
        }

        holder.setText(R.id.zan_num,pingjia.getUser_click_comment_up_count()+"");
        holder.setText(R.id.down_num,pingjia.getUser_click_comment_down_count()+"");
        holder.setText(R.id.intr_num,pingjia.getUser_click_comment_equal_count()+"");
        holder.setText(R.id.pinglun_num,pingjia.getComment_count()+"");

        ((ImageView)holder.getView(R.id.zan_img)).setImageResource(R.mipmap.good_icon);
        ((ImageView)holder.getView(R.id.down_img)).setImageResource(R.mipmap.bad_icon);
        ((ImageView)holder.getView(R.id.intr_img)).setImageResource(R.mipmap.smile_icon);
        if (pingjia.getUser_click_comment().size()!=0){
           PingjiaBean.UserClickCommentBean userClick=pingjia.getUser_click_comment().get(0);
            if (userClick.getUp()==1){
                ((ImageView)holder.getView(R.id.zan_img)).setImageResource(R.mipmap.good_select_icon);
            }else if (userClick.getDown()==1){
                ((ImageView)holder.getView(R.id.down_img)).setImageResource(R.mipmap.bad_select_icon);
            }else if (userClick.getEqual()==1){
                ((ImageView)holder.getView(R.id.intr_img)).setImageResource(R.mipmap.smile_select_icon);
            }
        }
    }
}
