package capital.fbg.wallet.ui.market.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import capital.fbg.wallet.R;
import capital.fbg.wallet.bean.MarketAddBean;
import capital.fbg.wallet.bean.MarketBean;

/**
 * Created by Administrator on 2017/8/9.
 * 功能描述：
 * 版本：@version
 */

public class MarketAddAdapter extends CommonAdapter<MarketAddBean.DataBean> {

    public MarketAddAdapter(Context context, int layoutId, List<MarketAddBean.DataBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MarketAddBean.DataBean marketBean, int position) {
        if (marketBean.getType()==2){
            holder.setVisible(R.id.title,true);
            holder.setVisible(R.id.content,false);
            holder.setVisible(R.id.line,false);
            holder.setText(R.id.title,marketBean.getName());
        }else {
            holder.setVisible(R.id.title,false);
            holder.setVisible(R.id.content,true);
            holder.setVisible(R.id.line,true);
            if (null!=marketBean.getIcon()&&marketBean.getIcon().length()>0){
                Glide.with(mContext).load(marketBean.getIcon()).crossFade().into((ImageView) holder.getView(R.id.img));
            }
            holder.setText(R.id.name,marketBean.getName());

            if (0==marketBean.getRelation_user_count()){
                Glide.with(mContext).load(R.mipmap.list_btn_default).crossFade().into((ImageView) holder.getView(R.id.select));
            }else {
                Glide.with(mContext).load(R.mipmap.list_btn_selected).crossFade().into((ImageView) holder.getView(R.id.select));
            }
        }
    }
}
