package capital.fbg.wallet.ui.wallet.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.R;
import capital.fbg.wallet.bean.WalletBean;
import capital.fbg.wallet.common.Constant;

/**
 * Created by Administrator on 2017/8/9.
 * 功能描述：
 * 版本：@version
 */

public class WalletMenuAdapter extends CommonAdapter<WalletBean> {

    public WalletMenuAdapter(Context context, int layoutId, List<WalletBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, WalletBean walletBean, int position) {
        holder.setText(R.id.name,walletBean.getName());
        Glide.with(mContext).load(walletBean.getIcon()).crossFade().into((ImageView) holder.getView(R.id.img));
        if (null!=walletBean.getType()&&!"".equals(walletBean.getType())){
            switch (new Integer(walletBean.getType())){
                case 0:
                    holder.setVisible(R.id.state,true);
                    holder.setText(R.id.state,mContext.getString(R.string.weibeifen));
                    holder.setBackgroundRes(R.id.state,R.drawable.round_solid_pink_bg);
                    break;
                case 1:
                    if (AppApplication.get().getSp().getBoolean(Constant.IS_CLOD,false)){
                        holder.setVisible(R.id.state,true);
                        holder.setText(R.id.state,"冷钱包");
                        holder.setBackgroundRes(R.id.state,R.drawable.round_999dp_bule_bg);
                    }else {
                        holder.setVisible(R.id.state,false);
                    }
                    break;
                case 2:
                    holder.setVisible(R.id.state,true);
                    holder.setText(R.id.state,mContext.getString(R.string.guancha));
                    holder.setBackgroundRes(R.id.state,R.drawable.round_999dp_bule_bg);
                    break;
                case 3:
                    holder.setVisible(R.id.state,true);
                    holder.setText(R.id.state,mContext.getString(R.string.weibeifen));
                    holder.setBackgroundRes(R.id.state,R.drawable.round_solid_pink_bg);
                    break;
            }
        }
    }
}
