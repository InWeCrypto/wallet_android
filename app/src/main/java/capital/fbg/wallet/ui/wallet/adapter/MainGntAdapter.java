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

/**
 * Created by Administrator on 2017/7/15.
 * 功能描述：
 * 版本：@version
 */

public class MainGntAdapter extends CommonAdapter<TokenBean.ListBean> {

    public MainGntAdapter(Context context, int layoutId, List<TokenBean.ListBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TokenBean.ListBean gntBean, int position) {
        Glide.with(mContext).load(gntBean.getGnt_category().getIcon()).transform(new GlideCircleTransform(mContext)).into((ImageView) holder.getView(R.id.iv_img));
        holder.setText(R.id.name,gntBean.getName());
        BigDecimal currentPrice = new BigDecimal(gntBean.getBalance());
        holder.setText(R.id.tv_price,currentPrice.setScale(4,BigDecimal.ROUND_HALF_UP).toString());
        if (1== AppApplication.get().getUnit()){
            holder.setText(R.id.tv_eth_ch_price,"≈￥"+currentPrice.multiply(new BigDecimal(gntBean.getGnt_category().getCap().getPrice_cny())).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        }else {
            holder.setText(R.id.tv_eth_ch_price,"≈$"+currentPrice.multiply(new BigDecimal(gntBean.getGnt_category().getCap().getPrice_usd())).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        }
    }
}
