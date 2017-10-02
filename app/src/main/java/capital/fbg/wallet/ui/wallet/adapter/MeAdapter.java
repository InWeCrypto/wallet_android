package capital.fbg.wallet.ui.wallet.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import capital.fbg.wallet.R;
import capital.fbg.wallet.bean.MeMenuBean;

/**
 * Created by Administrator on 2017/8/28.
 * 功能描述：
 * 版本：@version
 */

public class MeAdapter extends CommonAdapter<MeMenuBean> {

    public MeAdapter(Context context, int layoutId, List<MeMenuBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MeMenuBean meMenuBean, int position) {
        holder.setText(R.id.title,meMenuBean.getTitle());
        Glide.with(mContext).load(meMenuBean.getIcon()).crossFade().into((ImageView) holder.getView(R.id.img));
    }
}
