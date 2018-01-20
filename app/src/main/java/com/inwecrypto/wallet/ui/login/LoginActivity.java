package com.inwecrypto.wallet.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.ui.newneo.MainActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import butterknife.BindView;

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
import com.inwecrypto.wallet.ui.me.activity.CommonWebActivity;
/**
 * Created by Administrator on 2017/7/15.
 * 功能描述：
 * 版本：@version
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.hot_qianbao)
    TextView hotQianbao;
    @BindView(R.id.tiaokuan)
    TextView tiaokuan;

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
                    ToastUtil.show(R.string.qingjianchawangluoshifoulianjie);
                    return;
                }
                login();
            }
        });

        tiaokuan.setText(Html.fromHtml(getString(R.string.fuwuxieyi_yingsixieyi_underline)));
        tiaokuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CommonWebActivity.class);
                intent.putExtra("title", getString(R.string.fuwuxieyiheyinsixieyi));
                intent.putExtra("url", Url.EULA);
                keepTogo(intent);
            }
        });

    }

    private void login() {

        showFixLoading();

        pushService = PushServiceFactory.getCloudPushService();

        if (null==pushService.getDeviceId()||pushService.getDeviceId().equals("")){
            if (App.isMain) {
                openID= App.get().getSp().getString(Constant.OPEN_ID);
            }else {
                openID= App.get().getSp().getString(Constant.TEST_OPEN_ID);
            }
        }else {
            openID= pushService.getDeviceId();
        }

        if (null==openID||openID.equals("")){
            pushService = PushServiceFactory.getCloudPushService();
            pushService.register(mActivity, new CommonCallback() {
                @Override
                public void onSuccess(String response) {
                    if (App.isMain) {
                        App.get().getSp().putString(Constant.OPEN_ID,pushService.getDeviceId());
                    }else {
                        App.get().getSp().putString(Constant.TEST_OPEN_ID,pushService.getDeviceId());
                    }
                    login();
                }
                @Override
                public void onFailed(String errorCode, String errorMessage) {
                    ToastUtil.show(R.string.idhuoqushibai);
                    hideFixLoading();
                    return;
                }

            });
        }else {
            UserApi.login(mActivity, openID, new JsonCallback<LzyResponse<LoginBean>>() {
                @Override
                public void onSuccess(Response<LzyResponse<LoginBean>> response) {
                    pushService.bindAccount(pushService.getDeviceId(), new CommonCallback() {
                        @Override
                        public void onSuccess(String s) {
                        }

                        @Override
                        public void onFailed(String s, String s1) {
                        }
                    });
                    if (null==response.body().data.getUser().getNickname()){
                        response.body().data.getUser().setNickname(response.body().data.getUser().getOpen_id().substring(0,12));
                    }
                    if (null==response.body().data.getUser().getImg()){
                        response.body().data.getUser().setImg("1");
                    }

                    if (App.isMain) {
                        App.get().getSp().putString(Constant.OPEN_ID,openID);
                        App.get().getSp().putString(Constant.TOKEN, response.body().data.getToken());

                    }else {
                        App.get().getSp().putString(Constant.TEST_OPEN_ID,openID);
                        App.get().getSp().putString(Constant.TEST_TOKEN, response.body().data.getToken());
                    }
                    App.get().getSp().putString(Constant.USER_INFO, GsonUtils.objToJson(response.body().data));
                    App.get().setLoginBean(response.body().data);
                    Intent intent=new Intent(mActivity,MainActivity.class);
                    //如果启动app的Intent中带有额外的参数，表明app是从点击通知栏的动作中启动的
                    //将参数取出，传递到MainActivity中
                    if(getIntent().getStringExtra("pushInfo") != null){
                        intent.putExtra("pushInfo",
                                getIntent().getStringExtra("pushInfo"));
                    }
                    finshTogo(intent);
                    hideFixLoading();
                }

                @Override
                public void onError(Response<LzyResponse<LoginBean>> response) {
                    super.onError(response);
                    ToastUtil.show(R.string.denglushibai);
                    hideFixLoading();
                }
            });
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //1.点击返回键条件成立
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            if (isFixLoadingShow()){
                OkGo.getInstance().cancelTag(this);
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

}
