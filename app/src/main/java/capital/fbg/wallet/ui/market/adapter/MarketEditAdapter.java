package capital.fbg.wallet.ui.market.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import capital.fbg.wallet.R;
import capital.fbg.wallet.bean.MarkeListBean;
import capital.fbg.wallet.bean.MarketBean;

/**
 * Created by Administrator on 2017/8/9.
 * 功能描述：
 * 版本：@version
 */

public class MarketEditAdapter extends CommonAdapter<MarkeListBean.DataBean> {

    public MarketEditAdapter(Context context, int layoutId, List<MarkeListBean.DataBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MarkeListBean.DataBean marketBean, int position) {
        holder.setText(R.id.name,marketBean.getName());
        holder.setText(R.id.net_name,marketBean.getSource());
    }
}
