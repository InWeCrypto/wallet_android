package capital.fbg.wallet.ui.market.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

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

public class MarketRemindAddAdapter extends CommonAdapter<MarketRemindBean> {

    public MarketRemindAddAdapter(Context context, int layoutId, List<MarketRemindBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final MarketRemindBean marketBean, final int position) {
        holder.setText(R.id.name,marketBean.getName());
        holder.setText(R.id.net_name,marketBean.getFlag());
        if (null!=marketBean.getRelationCap()){
            holder.setText(R.id.price,"￥"+new BigDecimal(marketBean.getRelationCap().getPrice_cny()).setScale(2,BigDecimal.ROUND_HALF_UP));
        }else {
            holder.setText(R.id.price,"暂无数据");

        }

        if (marketBean.getRelation_notification_count()==1){
            holder.setImageResource(R.id.select,R.mipmap.list_btn_selected);
            EditText up=holder.getView(R.id.up);
            up.setEnabled(true);
            if (up.getTag() instanceof TextWatcher) {
                up.removeTextChangedListener((TextWatcher)up.getTag());
            }
            if (marketBean.getRelation_notification().size()==0){
                up.setText(marketBean.getUp());
            }else {
                up.setText(marketBean.getRelation_notification().get(0).getUpper_limit());
            }
            TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (marketBean.getRelation_notification().size()==0){
                        marketBean.setUp(s.toString());
                    }else {
                        marketBean.getRelation_notification().get(0).setUpper_limit(s.toString());
                    }
                }
            };

            up.addTextChangedListener(watcher);
            up.setTag(watcher);

            EditText down=holder.getView(R.id.down);
            down.setEnabled(true);
            if (down.getTag() instanceof TextWatcher) {
                down.removeTextChangedListener((TextWatcher)down.getTag());
            }
            if (marketBean.getRelation_notification().size()==0){
                down.setText(marketBean.getLow());
            }else {
                down.setText(marketBean.getRelation_notification().get(0).getLower_limit());
            }
            TextWatcher downwatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    marketBean.setLow(s.toString());
                    if (marketBean.getRelation_notification().size()==0){
                        marketBean.setLow(s.toString());
                    }else {
                        marketBean.getRelation_notification().get(0).setLower_limit(s.toString());
                    }
                }
            };

            down.addTextChangedListener(downwatcher);
            down.setTag(downwatcher);
        }else {
            holder.setImageResource(R.id.select,R.mipmap.list_btn_default);
            EditText up=holder.getView(R.id.up);
            up.setEnabled(false);
            if (up.getTag() instanceof TextWatcher) {
                up.removeTextChangedListener((TextWatcher)up.getTag());
            }
            up.setText("");
            EditText down=holder.getView(R.id.down);
            down.setEnabled(false);
            if (down.getTag() instanceof TextWatcher) {
                down.removeTextChangedListener((TextWatcher)down.getTag());
            }
            down.setText("");
        }
    }
}
