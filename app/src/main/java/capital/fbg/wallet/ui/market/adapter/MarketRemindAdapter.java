package capital.fbg.wallet.ui.market.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.util.List;

import capital.fbg.wallet.R;
import capital.fbg.wallet.bean.MarketRemindBean;

/**
 * Created by Administrator on 2017/8/12.
 * 功能描述：
 * 版本：@version
 */

public class MarketRemindAdapter extends CommonAdapter<MarketRemindBean> {

    public MarketRemindAdapter(Context context, int layoutId, List<MarketRemindBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MarketRemindBean marketRemindBean, int position) {
        holder.setText(R.id.name,marketRemindBean.getName());
        holder.setText(R.id.net_name,marketRemindBean.getFlag());
        if (null!=marketRemindBean.getRelationCap()){
            holder.setText(R.id.price,"￥"+new BigDecimal(marketRemindBean.getRelationCap().getPrice_cny()).setScale(2,BigDecimal.ROUND_HALF_UP));
        }else {
            holder.setText(R.id.price,"暂无数据");

        }
        if (null!=marketRemindBean.getRelation_notification()){
            holder.setText(R.id.up,marketRemindBean.getRelation_notification().get(0).getUpper_limit());
            holder.setText(R.id.down,marketRemindBean.getRelation_notification().get(0).getLower_limit());
        }
    }
}
