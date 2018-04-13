package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.PingjiaBean;
import com.inwecrypto.wallet.bean.TradingProjectDetaileBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.RatingBar;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.activity.MyPingjiaActivity;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/4/4 17:03
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class PingjiaXiangmuActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.fenshu)
    TextView fenshu;
    @BindView(R.id.ratingbar)
    RatingBar ratingbar;
    @BindView(R.id.henbumanyi)
    TextView henbumanyi;
    @BindView(R.id.bumanyi)
    TextView bumanyi;
    @BindView(R.id.yiban)
    TextView yiban;
    @BindView(R.id.tuijian)
    TextView tuijian;
    @BindView(R.id.feichangtuijian)
    TextView feichangtuijian;
    @BindView(R.id.cantouguo)
    LinearLayout cantouguo;
    @BindView(R.id.xiangcantou)
    LinearLayout xiangcantou;
    @BindView(R.id.weiguan)
    LinearLayout weiguan;
    @BindView(R.id.et_info)
    EditText etInfo;

    private TradingProjectDetaileBean project;
    private int type=0;
    private float score=5.0f;

    @Override
    protected void getBundleExtras(Bundle extras) {
        project = (TradingProjectDetaileBean) extras.getSerializable("project");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.pingjiaxiangmu_activity_layout;
    }

    @Override
    protected void initView() {

        txtMainTitle.setText(project.getName()+getString(R.string.pingjia));

        ratingbar.setStar(score);

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtRightTitle.setText(R.string.fabu);
        txtRightTitle.setCompoundDrawables(null, null, null, null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etInfo.getText().toString().trim().length()>0&&etInfo.getText().toString().trim().length()<10){
                    ToastUtil.show(getString(R.string.pinglunbunengshaoyu10));
                    return;
                }
                showFixLoading();
                ZixunApi.postComment(this
                        , project.getId()+""
                        , score
                        , etInfo.getText().toString()
                        , type
                        , new JsonCallback<LzyResponse<PingjiaBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<PingjiaBean>> response) {
                        Intent intent=new Intent(mActivity,MyPingjiaActivity.class);
                        intent.putExtra("pingjia",response.body().data);
                        intent.putExtra("isFromMe",false);
                        finshTogo(intent);
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PINGJIA_SUC));
                    }

                    @Override
                    public void onError(Response<LzyResponse<PingjiaBean>> response) {
                        super.onError(response);
                        ToastUtil.show(R.string.caozuoshibai);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideFixLoading();
                    }
                });

            }
        });

        ratingbar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                score=ratingCount;
                fenshu.setText(ratingCount+"");
                henbumanyi.setTextColor(Color.parseColor("#ACACAC"));
                bumanyi.setTextColor(Color.parseColor("#ACACAC"));
                yiban.setTextColor(Color.parseColor("#ACACAC"));
                tuijian.setTextColor(Color.parseColor("#ACACAC"));
                feichangtuijian.setTextColor(Color.parseColor("#ACACAC"));

                if (ratingCount<=1){
                    henbumanyi.setTextColor(Color.parseColor("#FF7E00"));
                }else if (ratingCount<=2){
                    bumanyi.setTextColor(Color.parseColor("#FF7E00"));
                }else if (ratingCount<=3){
                    yiban.setTextColor(Color.parseColor("#FF7E00"));
                }else if (ratingCount<=4){
                    tuijian.setTextColor(Color.parseColor("#FF7E00"));
                }else if (ratingCount<=5){
                    feichangtuijian.setTextColor(Color.parseColor("#FF7E00"));
                }
            }
        });

        cantouguo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type==1){
                    cantouguo.setBackgroundResource(R.drawable.pingjia_type_bg);
                    type=0;
                    return;
                }
                type=1;
                cantouguo.setBackgroundResource(R.drawable.pingjia_type_select_bg);
                xiangcantou.setBackgroundResource(R.drawable.pingjia_type_bg);
                weiguan.setBackgroundResource(R.drawable.pingjia_type_bg);
            }
        });

        xiangcantou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type==2){
                    xiangcantou.setBackgroundResource(R.drawable.pingjia_type_bg);
                    type=0;
                    return;
                }
                type=2;
                xiangcantou.setBackgroundResource(R.drawable.pingjia_type_select_bg);
                cantouguo.setBackgroundResource(R.drawable.pingjia_type_bg);
                weiguan.setBackgroundResource(R.drawable.pingjia_type_bg);
            }
        });

        weiguan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type==3){
                    weiguan.setBackgroundResource(R.drawable.pingjia_type_bg);
                    type=0;
                    return;
                }
                type=3;
                weiguan.setBackgroundResource(R.drawable.pingjia_type_select_bg);
                cantouguo.setBackgroundResource(R.drawable.pingjia_type_bg);
                xiangcantou.setBackgroundResource(R.drawable.pingjia_type_bg);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
