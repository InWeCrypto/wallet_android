package capital.fbg.wallet.ui.discover.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import capital.fbg.wallet.R;
import capital.fbg.wallet.bean.FindBean;
import capital.fbg.wallet.common.http.Url;
import capital.fbg.wallet.common.imageloader.GlideCircleTransform;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class IcoAdapter extends CommonAdapter<FindBean.Ico> {

    public IcoAdapter(Context context, int layoutId, List<FindBean.Ico> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, FindBean.Ico ico, int position) {
        holder.setText(R.id.name,ico.getName());
        Glide.with(mContext)
                .load(ico.getImg())
                .crossFade()
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.img));
    }

}
