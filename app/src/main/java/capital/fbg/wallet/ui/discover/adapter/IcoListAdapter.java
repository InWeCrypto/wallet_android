package capital.fbg.wallet.ui.discover.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import capital.fbg.wallet.R;
import capital.fbg.wallet.bean.IcoListBean;
import capital.fbg.wallet.common.util.ScreenUtils;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class IcoListAdapter extends CommonAdapter<IcoListBean> {

    private int width;
    private int height;

    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    //用于退出activity,避免countdown，造成资源浪费。
    private SparseArray<CountDownTimer> countDownMap;

    public IcoListAdapter(Context context, int layoutId, List<IcoListBean> datas) {
        super(context, layoutId, datas);
        width= ScreenUtils.getScreenWidth(context);
        height= (int) (width/750.0f*412);
        countDownMap = new SparseArray<>();
    }

    @Override
    protected void convert(final ViewHolder holder, IcoListBean icoListBean, int position) {
        holder.setText(R.id.title,icoListBean.getTitle());
        holder.setText(R.id.intro,icoListBean.getIntro());
        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams)holder.getView(R.id.img).getLayoutParams();
        params.width=width;
        params.height=height;
        holder.getView(R.id.img).setLayoutParams(params);

        Glide.with(mContext)
                .load(icoListBean.getImg())
                .crossFade()
                .into((ImageView) holder.getView(R.id.img));

        if (null==icoListBean.getStart_at()||null==icoListBean.getEnd_at()){
            return;
        }
        long time=-1;
        if (System.currentTimeMillis()-new Date(Long.parseLong(icoListBean.getStart_at())).getTime()>0){
            time=System.currentTimeMillis()-new Date(Long.parseLong(icoListBean.getStart_at())).getTime();
            //即将开始
            holder.setBackgroundColor(R.id.state, Color.parseColor("#66b7fb "));
            holder.setText(R.id.state,"即将开始");
        }else if (new Date(Long.parseLong(icoListBean.getEnd_at())).getTime()-System.currentTimeMillis()>0){
            time=new Date(Long.parseLong(icoListBean.getEnd_at())).getTime()-System.currentTimeMillis();
            //众筹中
            holder.setBackgroundColor(R.id.state, Color.parseColor("#fdd930 "));
            holder.setText(R.id.state,"众筹中");
        }else {
            //已完成
            holder.setBackgroundColor(R.id.state, Color.parseColor("#efefef "));
            holder.setText(R.id.state,"已结束");
            holder.setText(R.id.time,"剩余 00:00:00");
        }

        if (time > 0) {
            CountDownTimer countDownTimer = new CountDownTimer(time, 1000) {
                public void onTick(long millisUntilFinished) {
                    holder.setText(R.id.time,formatter.format(new Date(millisUntilFinished)));
                }
                public void onFinish() {
                    holder.setText(R.id.time,"剩余 00:00:00");
                    holder.setBackgroundColor(R.id.state, Color.parseColor("#efefef "));
                    holder.setText(R.id.state,"已结束");
                }
            }.start();

            countDownMap.put(holder.getView(R.id.time).hashCode(), countDownTimer);
        }
    }

    /**
     * 清空资源
     */
    public void cancelAllTimers() {
        if (countDownMap == null) {
            return;
        }
        for (int i = 0,length = countDownMap.size(); i < length; i++) {
            CountDownTimer cdt = countDownMap.get(countDownMap.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }

}
