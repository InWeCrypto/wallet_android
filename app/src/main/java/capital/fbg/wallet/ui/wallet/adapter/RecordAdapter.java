package capital.fbg.wallet.ui.wallet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.util.List;

import capital.fbg.wallet.R;
import capital.fbg.wallet.bean.OrderBean;
import capital.fbg.wallet.ui.wallet.activity.TokenWalletActivity;

/**
 * Created by Administrator on 2017/8/10.
 * 功能描述：
 * 版本：@version
 */

public class RecordAdapter extends CommonAdapter<OrderBean> {

    private String address;
    private TokenWalletActivity activity;
    private BigDecimal pEther = new BigDecimal("1000000000000000000");
    private String unit;

    public RecordAdapter(Context context, int layoutId, List<OrderBean> datas,String address,String unit) {
        super(context, layoutId, datas);
        this.address=address;
        activity= (TokenWalletActivity) context;
        this.unit=unit;
    }

    @Override
    protected void convert(ViewHolder holder, OrderBean orderBean, int position) {
        if (orderBean.getPay_address().equals(address)){
            if (orderBean.getStatus()==0){//交易失败
                Glide.with(mContext).load(R.mipmap.tishi).crossFade().into((ImageView) holder.getView(R.id.img));
                holder.setText(R.id.hit,"交易失败");
                holder.setVisible(R.id.progess,false);
            }else if (orderBean.getStatus()==1){//准备打包
                Glide.with(mContext).load(R.mipmap.zhuanchu).crossFade().into((ImageView) holder.getView(R.id.img));
                holder.setText(R.id.hit,"准备打包");
                holder.setVisible(R.id.progess,true);
                ProgressBar bar=holder.getView(R.id.progess);
                bar.setProgress(0);
            }else if (orderBean.getStatus()==2){//打包中
                Glide.with(mContext).load(R.mipmap.zhuanchu).crossFade().into((ImageView) holder.getView(R.id.img));
                holder.setVisible(R.id.progess,true);
                BigDecimal currentPrice=new BigDecimal(0);
                if (null!=orderBean.getBlock_number()){
                    currentPrice = new BigDecimal(orderBean.getBlock_number());
                }
                int current= (int) (activity.currentBlock-currentPrice.doubleValue())+1;
                if (current<0){
                    current=0;
                }
                holder.setText(R.id.hit,"已经确认"+current+"/"+activity.minBlock);
                ProgressBar bar=holder.getView(R.id.progess);
                bar.setProgress((int) (current*1.0f/activity.minBlock*100.f));
                if (current>=activity.minBlock){
                    orderBean.setStatus(3);
                    Glide.with(mContext).load(R.mipmap.zhuanchu).crossFade().into((ImageView) holder.getView(R.id.img));
                    holder.setText(R.id.hit,"");
                    holder.setVisible(R.id.progess,false);
                }
            }else if (orderBean.getStatus()==3){//交易成功
                Glide.with(mContext).load(R.mipmap.zhuanchu).crossFade().into((ImageView) holder.getView(R.id.img));
                holder.setText(R.id.hit,"");
                holder.setVisible(R.id.progess,false);
            }
            holder.setText(R.id.price,"-"+new BigDecimal(orderBean.getFee()).divide(pEther,4,BigDecimal.ROUND_HALF_UP).toPlainString()+unit);
            holder.setTextColor(R.id.price, Color.parseColor("#F81A1A"));
        }else {
            if (orderBean.getStatus()==0){//交易失败
                Glide.with(mContext).load(R.mipmap.tishi).crossFade().into((ImageView) holder.getView(R.id.img));
                holder.setText(R.id.hit,"交易失败");
                holder.setVisible(R.id.progess,false);
            }else if (orderBean.getStatus()==1){//准备打包
                Glide.with(mContext).load(R.mipmap.zhuanru).crossFade().into((ImageView) holder.getView(R.id.img));
                holder.setText(R.id.hit,"准备打包");
                holder.setVisible(R.id.progess,true);
                ProgressBar bar=holder.getView(R.id.progess);
                bar.setProgress(0);
            }else if (orderBean.getStatus()==2){//打包中
                Glide.with(mContext).load(R.mipmap.zhuanru).crossFade().into((ImageView) holder.getView(R.id.img));
                holder.setVisible(R.id.progess,true);
                BigDecimal currentPrice=new BigDecimal(0);
                if (null!=orderBean.getBlock_number()){
                    currentPrice = new BigDecimal(orderBean.getBlock_number());
                }
                int current=(int) (activity.currentBlock-currentPrice.doubleValue())+1;
                if (current<0){
                    current=0;
                }
                holder.setText(R.id.hit,"已经确认"+current+"/"+activity.minBlock);
                ProgressBar bar=holder.getView(R.id.progess);
                bar.setProgress((int) (current*1.0f/activity.minBlock*100.f));
                if (current>=activity.minBlock){
                    orderBean.setStatus(3);
                    Glide.with(mContext).load(R.mipmap.zhuanru).crossFade().into((ImageView) holder.getView(R.id.img));
                    holder.setText(R.id.hit,"");
                    holder.setVisible(R.id.progess,false);
                }
            }else if (orderBean.getStatus()==3){//交易成功
                Glide.with(mContext).load(R.mipmap.zhuanru).crossFade().into((ImageView) holder.getView(R.id.img));
                holder.setText(R.id.hit,"");
                holder.setVisible(R.id.progess,false);
            }
            holder.setText(R.id.price,"+"+new BigDecimal(orderBean.getFee()).divide(pEther,4,BigDecimal.ROUND_HALF_UP).toPlainString()+unit);
            holder.setTextColorRes(R.id.price,R.color.c_232772);
        }
        holder.setText(R.id.order,orderBean.getTrade_no());
        holder.setText(R.id.time,orderBean.getCreated_at());
    }
}
