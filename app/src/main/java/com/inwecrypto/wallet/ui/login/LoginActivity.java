package com.inwecrypto.wallet.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import com.inwecrypto.wallet.AppApplication;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.LoginBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.UserApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.MainTabActivity;
import com.inwecrypto.wallet.ui.me.activity.CommonWebActivity;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2017/7/15.
 * 功能描述：
 * 版本：@version
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.hot_qianbao)
    TextView hotQianbao;
    @BindView(R.id.cold_qianbao)
    TextView coldQianbao;
    @BindView(R.id.tiaokuan)
    TextView tiaokuan;

    private MaterialDialog mMaterialDialog;
    private CloudPushService pushService;
    private String openID="";


    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.login_activity;
    }

    @Override
    protected void initView() {
        hotQianbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(mActivity)){
                    ToastUtil.show("请检查网络是否连接");
                    return;
                }
                login();
            }
        });

        tiaokuan.setText(Html.fromHtml("<u>《服务协议》、《隐私协议》</u>"));
        tiaokuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CommonWebActivity.class);
                intent.putExtra("title", "服务协议和隐私条款");
                intent.putExtra("url", Url.EULA);
                keepTogo(intent);
            }
        });

        coldQianbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coldLogin();
            }
        });

    }

    private void login() {
        showLoading();
        pushService = PushServiceFactory.getCloudPushService();
        if (null==pushService.getDeviceId()||pushService.getDeviceId().equals("")){
            openID=AppApplication.get().getSp().getString(Constant.OPEN_ID);
        }else {
            openID= pushService.getDeviceId();
        }
        if (null==openID||openID.equals("")){
            pushService = PushServiceFactory.getCloudPushService();
            pushService.register(mActivity, new CommonCallback() {
                @Override
                public void onSuccess(String response) {
                    AppApplication.get().getSp().putString(Constant.OPEN_ID,pushService.getDeviceId());
                    login();
                }
                @Override
                public void onFailed(String errorCode, String errorMessage) {
                    ToastUtil.show("ID获取失败，请检查网络后重试");
                    return;
                }
            });
            return;
        }
        UserApi.login(mActivity, openID, new JsonCallback<LzyResponse<LoginBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<LoginBean>> response) {
                hideLoading();
                pushService.bindAccount(pushService.getDeviceId(), new CommonCallback() {
                    @Override
                    public void onSuccess(String s) {
                    }

                    @Override
                    public void onFailed(String s, String s1) {
                    }
                });
                AppApplication.get().getSp().putString(Constant.TOKEN, response.body().data.getToken());
                if (null==response.body().data.getUser().getNickname()){
                    response.body().data.getUser().setNickname(response.body().data.getUser().getOpen_id().substring(0,12));
                }
                if (null==response.body().data.getUser().getImg()){
                    response.body().data.getUser().setImg("1");
                }
                AppApplication.get().getSp().putString(Constant.USER_INFO, GsonUtils.objToJson(response.body().data));
                AppApplication.get().setLoginBean(response.body().data);
                Intent intent=new Intent(mActivity,MainTabActivity.class);
                //如果启动app的Intent中带有额外的参数，表明app是从点击通知栏的动作中启动的
                //将参数取出，传递到MainActivity中
                if(getIntent().getStringExtra("pushInfo") != null){
                    intent.putExtra("pushInfo",
                            getIntent().getStringExtra("pushInfo"));
                }
                finshTogo(intent);
            }

            @Override
            public void onError(Response<LzyResponse<LoginBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.login_error));
                hideLoading();
            }
        });
    }

    private void coldLogin() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_login_clode, null, false);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);
        TextView ok = (TextView) view.findViewById(R.id.ok);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isConnected(mActivity)) {
                    ToastUtil.show("请确保手机处于飞行模式！");
                    return;
                } else {
                    mMaterialDialog.dismiss();
                    AppApplication.get().getSp().putBoolean(Constant.NEED_RESTART, true);
                    AppApplication.get().getSp().putBoolean(Constant.IS_CLOD, true);
                    finshTogo(MainTabActivity.class);
                }
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });

        mMaterialDialog = new MaterialDialog(mActivity).setView(view);
        mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.show();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
