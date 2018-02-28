package com.inwecrypto.wallet.ui.me.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.UserApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import com.lzy.okgo.model.Response;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/2/7 10:49
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class FixPassActivity extends BaseActivity {


    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.et_oldpassword)
    EditText etOldpassword;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.pass_see)
    ImageView passSee;
    @BindView(R.id.et_password_two)
    EditText etPasswordTwo;
    private boolean isSee;


    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.fix_pass_activity;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.xiugaimima);
        txtRightTitle.setText(R.string.baocun);
        txtRightTitle.setCompoundDrawables(null, null, null, null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(mActivity)) {
                    ToastUtil.show(R.string.qingjianchawangluoshifoulianjie);
                    return;
                }

                if (etOldpassword.getText().toString().trim().length() == 0) {
                    ToastUtil.show(getString(R.string.jiumimabunengweikong));
                    return;
                }

                if (etPassword.getText().toString().trim().length() == 0) {
                    ToastUtil.show(getString(R.string.mimabunengweikong));
                    return;
                }

                if (etPasswordTwo.getText().toString().trim().length() == 0) {
                    ToastUtil.show(getString(R.string.mimabunengweikong));
                    return;
                }

                if (!etPassword.getText().toString().trim().equals(etPasswordTwo.getText().toString().trim())) {
                    ToastUtil.show(getString(R.string.liangcimimabuxiangtong));
                    return;
                }

                showFixLoading();
                fixPass();
            }
        });

        passSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSee) {
                    isSee = false;
                    passSee.setImageResource(R.mipmap.denglu_eyes_close_xxx);
                    //否则隐藏密码
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPasswordTwo.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    isSee = true;
                    passSee.setImageResource(R.mipmap.denglu_eyes_open_xxx);
                    //如果选中，显示密码
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //如果选中，显示密码
                    etPasswordTwo.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                etPassword.setSelection(etPassword.getText().length());
                etPasswordTwo.setSelection(etPasswordTwo.getText().length());
            }
        });


    }

    private void fixPass() {
        UserApi.resetPassword(this
                ,etOldpassword.getText().toString().trim()
                ,etPassword.getText().toString().trim()
                ,etPasswordTwo.getText().toString().trim()
                , new JsonCallback<LzyResponse<Object>>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(Response<LzyResponse<Object>> response) {
                        ToastUtil.show(getString(R.string.xiugaimimachenggong));
                        EMClient.getInstance().logout(true);
                        App.get().getSp().putString(Constant.TOKEN,"");
                        App.get().getSp().putString(Constant.TEST_TOKEN,"");
                        AppManager.getAppManager().finishAllActivity();
                        Intent intent=new Intent(mActivity, LoginActivity.class);
                        finshTogo(intent);
                    }

                    @Override
                    public void onError(Response<LzyResponse<Object>> response) {
                        super.onError(response);
                        if (response.getException().getMessage().contains("4004")){
                            ToastUtil.show(getString(R.string.mimazuixiaochangdu));
                        }else {
                            ToastUtil.show(getString(R.string.xiugaimimashibai));
                        }
                        return;
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideFixLoading();
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
