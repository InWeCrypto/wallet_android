package com.inwecrypto.wallet.ui.me.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import com.inwecrypto.wallet.AppApplication;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.LoginBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MeApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/8/12.
 * 功能描述：
 * 版本：@version
 */

public class FixSexActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.select1)
    ImageView select1;
    @BindView(R.id.rl_nan)
    RelativeLayout rlNan;
    @BindView(R.id.select2)
    ImageView select2;
    @BindView(R.id.rl_nv)
    RelativeLayout rlNv;

    private LoginBean user;
    private int sex;

    @Override
    protected void getBundleExtras(Bundle extras) {
        user = (LoginBean) extras.getSerializable("user");
        sex=user.getUser().getSex();
    }

    @Override
    protected int setLayoutID() {
        return R.layout.me_activity_fixsex;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.xiugaixingbie);
        txtRightTitle.setText(R.string.baocun);
        txtRightTitle.setCompoundDrawables(null, null, null, null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                MeApi.setUserInfo(mActivity, user.getUser().getNickname()==null?"":user.getUser().getNickname(), user.getUser().getImg(), sex, new JsonCallback<LzyResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<Object>> response) {
                        hideLoading();
                        AppApplication.get().getLoginBean().getUser().setSex(sex);
                        AppApplication.get().getSp().putString(Constant.USER_INFO, GsonUtils.objToJson(user));
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_USERINFO));
                        ToastUtil.show(R.string.xiugaichenggong);
                        finish();
                    }

                    @Override
                    public void onError(Response<LzyResponse<Object>> response) {
                        super.onError(response);
                        hideLoading();
                        ToastUtil.show(R.string.xiugaishibai);
                    }
                });
            }
        });

        rlNan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(mActivity).load(R.mipmap.list_btn_default).crossFade().into(select2);
                Glide.with(mActivity).load(R.mipmap.list_btn_selected).crossFade().into(select1);
                sex=1;
            }
        });
        rlNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(mActivity).load(R.mipmap.list_btn_default).crossFade().into(select1);
                Glide.with(mActivity).load(R.mipmap.list_btn_selected).crossFade().into(select2);
                sex=2;
            }
        });
        if (sex==1){
            Glide.with(mActivity).load(R.mipmap.list_btn_default).crossFade().into(select2);
            Glide.with(mActivity).load(R.mipmap.list_btn_selected).crossFade().into(select1);
        }else {
            Glide.with(mActivity).load(R.mipmap.list_btn_default).crossFade().into(select1);
            Glide.with(mActivity).load(R.mipmap.list_btn_selected).crossFade().into(select2);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
