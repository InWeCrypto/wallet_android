package com.inwecrypto.wallet.ui.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.UserApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.MainTabActivity;
import com.inwecrypto.wallet.ui.me.activity.CommonWebActivity;
import com.inwecrypto.wallet.ui.newneo.WalletFragment;
import com.lzy.okgo.model.Response;

import net.qiujuer.genius.ui.widget.Button;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/2/6 11:27
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class RegistActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.get_code)
    TextView getCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.pass_see)
    ImageView passSee;
    @BindView(R.id.et_password_two)
    EditText etPasswordTwo;
    @BindView(R.id.regist)
    Button regist;
    @BindView(R.id.service)
    TextView service;

    private boolean isSee;

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            getCode.setText(millisUntilFinished/1000 + getString(R.string.miao));
        }

        @Override
        public void onFinish() {
            getCode.setEnabled(true);
            getCode.setText(R.string.fasongyanzhengma);
        }
    };

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.regist_activity;
    }

    @Override
    protected void initView() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(mActivity)) {
                    ToastUtil.show(R.string.qingjianchawangluoshifoulianjie);
                    return;
                }

                if (email.getText().toString().trim().length()==0){
                    ToastUtil.show(getString(R.string.youxiangbunengweikong));
                    return;
                }

                getCode();
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

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(mActivity)) {
                    ToastUtil.show(R.string.qingjianchawangluoshifoulianjie);
                    return;
                }

                if (email.getText().toString().trim().length()==0){
                    ToastUtil.show(getString(R.string.youxiangbunengweikong));
                    return;
                }

                if (code.getText().toString().trim().length()==0){
                    ToastUtil.show(getString(R.string.yanzhengmabunengweikong));
                    return;
                }

                if (etPassword.getText().toString().trim().length()==0){
                    ToastUtil.show(getString(R.string.mimabunengweikong));
                    return;
                }

                if (etPasswordTwo.getText().toString().trim().length()==0){
                    ToastUtil.show(getString(R.string.mimabunengweikong));
                    return;
                }

                if (!etPassword.getText().toString().trim().equals(etPasswordTwo.getText().toString().trim())){
                    ToastUtil.show(getString(R.string.liangcimimabuxiangtong));
                    return;
                }

                regist();
            }
        });

        service.setText(Html.fromHtml(getString(R.string.fuwutiaokuan)));
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CommonWebActivity.class);
                intent.putExtra("title", getString(R.string.fuwuxieyiheyinsixieyi));
                intent.putExtra("url", Url.EULA);
                keepTogo(intent);
            }
        });

    }

    private void getCode() {
        showFixLoading();
        UserApi.getCode(this
                , email.getText().toString().trim()
                , new JsonCallback<LzyResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<Object>> response) {
                        ToastUtil.show(getString(R.string.yanzhengmafasongchenggongqingdaoyouxiangchakan));
                        getCode.setEnabled(false);
                        timer.start();
                        return;
                    }

                    @Override
                    public void onError(Response<LzyResponse<Object>> response) {
                        super.onError(response);
                        ToastUtil.show(getString(R.string.yanzhengmafasongshibai));
                        return;
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideFixLoading();
                    }
                });
    }

    private void regist() {

        showFixLoading();
        UserApi.register(this
                , code.getText().toString().trim()
                , email.getText().toString().trim()
                , email.getText().toString().trim()
                , etPassword.getText().toString().trim()
                , etPasswordTwo.getText().toString().trim()
                , new JsonCallback<LzyResponse<LoginBean>>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(Response<LzyResponse<LoginBean>> response) {

                        AppManager.getAppManager().finishActivity(LoginActivity.class);
                        if (App.isMain){
                            App.get().getSp().putString(Constant.TOKEN, response.body().data.getToken());
                        }else {
                            App.get().getSp().putString(Constant.TEST_TOKEN, response.body().data.getToken());
                        }
                        App.get().getSp().putString(Constant.USER_INFO, GsonUtils.objToJson(response.body().data));
                        App.get().setLoginBean(response.body().data);

                        Intent intent=new Intent(mActivity,MainTabActivity.class);
                        intent.putExtra("isYaoqin",true);
                        finshTogo(intent);
                    }

                    @Override
                    public void onError(Response<LzyResponse<LoginBean>> response) {
                        super.onError(response);
                        ToastUtil.show(getString(R.string.zhuceshibai));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

}
