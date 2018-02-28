package com.inwecrypto.wallet.ui.me.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.LoginBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MeApi;
import com.inwecrypto.wallet.common.http.api.UserApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/12.
 * 功能描述：
 * 版本：@version
 */

public class FixNameActivity extends BaseActivity {


    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.clear)
    ImageView clear;
    private LoginBean user;

    @Override
    protected void getBundleExtras(Bundle extras) {
    }

    @Override
    protected int setLayoutID() {
        return R.layout.me_activity_fixname;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.shezhinineng);
        txtRightTitle.setText(R.string.baocun);
        txtRightTitle.setCompoundDrawables(null, null, null, null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().length() == 0) {
                    ToastUtil.show(R.string.nichengbunengweikong);
                    return;
                }
                if (etName.getText().toString().length() > 12) {
                    ToastUtil.show(R.string.nichengbunengchaoguo12gezifu);
                    return;
                }
                showFixLoading();
                UserApi.setUserInfo(this
                        , null == user.getImg() ? "" : user.getImg()
                        , etName.getText().toString().trim()
                        , new JsonCallback<LzyResponse<Object>>() {
                            @Override
                            public void onSuccess(Response<LzyResponse<Object>> response) {
                                App.get().getLoginBean().setName(etName.getText().toString());
                                App.get().getSp().putString(Constant.USER_INFO, GsonUtils.objToJson(user));
                                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_USERINFO));
                                ToastUtil.show(R.string.xiugaichenggong);
                                finish();
                            }

                            @Override
                            public void onError(Response<LzyResponse<Object>> response) {
                                super.onError(response);
                                ToastUtil.show(R.string.xiugaishibai);
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                hideFixLoading();
                            }
                        });
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText("");
            }
        });
        user = App.get().getLoginBean();
    }

    @Override
    protected void initData() {
        etName.setText(user.getName());
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
