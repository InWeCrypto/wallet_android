package capital.fbg.wallet.ui.wallet.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.util.List;

import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.R;
import capital.fbg.wallet.bean.TokenBean;
import capital.fbg.wallet.common.imageloader.GlideCircleTransform;
import capital.fbg.wallet.common.util.AppUtil;

/**
 * Created by Administrator on 2017/7/15.
 * 功能描述：
 * 版本：@version
 */

public class GntAdapter extends CommonAdapter<TokenBean.ListBean> {

    private BigDecimal totleEther=new BigDecimal("0.00");
    private BigDecimal totlePrice=new BigDecimal("0.00");
    private BigDecimal pEther= new java.math.BigDecimal("1000000000000000000");

    public GntAdapter(Context context, int layoutId, List<TokenBean.ListBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TokenBean.ListBean gntBean, int position) {
        Glide.with(mContext).load(gntBean.getGnt_category().getIcon()).transform(new GlideCircleTransform(mContext)).crossFade().into((ImageView) holder.getView(R.id.iv_img));
        holder.setText(R.id.name,gntBean.getName());
        BigDecimal currentPrice = new BigDecimal(AppUtil.toD(gntBean.getBalance().replace("0x", "0")));
        holder.setText(R.id.tv_price,currentPrice.divide(pEther,4,BigDecimal.ROUND_HALF_UP).toString());
        if (1== AppApplication.get().getUnit()){
            holder.setText(R.id.tv_eth_ch_price,"≈￥"+currentPrice.divide(pEther).multiply(new BigDecimal(gntBean.getGnt_category().getCap().getPrice_cny())).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        }else {
            holder.setText(R.id.tv_eth_ch_price,"≈$"+currentPrice.divide(pEther).multiply(new BigDecimal(gntBean.getGnt_category().getCap().getPrice_usd())).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        }
    }
}
