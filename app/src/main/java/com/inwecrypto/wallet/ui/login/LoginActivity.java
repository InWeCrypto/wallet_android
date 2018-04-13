package com.inwecrypto.wallet.ui.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonProjectBean;
import com.inwecrypto.wallet.bean.LoginBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.UserApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.CacheUtils;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.LocaleUtils;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.MainTabActivity;
import com.inwecrypto.wallet.ui.newneo.WalletFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import net.qiujuer.genius.ui.widget.Button;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/15.
 * 功能描述：
 * 版本：@version
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.pass_see)
    ImageView passSee;
    @BindView(R.id.pass_loss)
    TextView passLoss;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.regist)
    TextView regist;

    private boolean isSee;


    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.login_activity;
    }

    @Override
    protected void initView() {

        login.setOnClickListener(new View.OnClickListener() {
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

                if (etPassword.getText().toString().trim().length()==0){
                    ToastUtil.show(getString(R.string.mimabunengweikong));
                    return;
                }

                login();
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
                } else {
                    isSee = true;
                    passSee.setImageResource(R.mipmap.denglu_eyes_open_xxx);
                    //如果选中，显示密码
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //如果选中，显示密码
                }
                etPassword.setSelection(etPassword.getText().length());
            }
        });


        regist.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,RegistActivity.class);
                keepTogo(intent);
            }
        });


        passLoss.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,FrogetPassActivty.class);
                keepTogo(intent);
            }
        });

        email.setText(App.get().getSp().getString(Constant.LOGIN_NAME+App.isMain,""));

    }

    private void login() {

        showFixLoading();

        UserApi.login(this
                , email.getText().toString().trim()
                , etPassword.getText().toString().trim()
                , new JsonCallback<LzyResponse<LoginBean>>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(final Response<LzyResponse<LoginBean>> response) {
                        //环信登录
                        EMClient.getInstance().login(response.body().data.getId()+"",etPassword.getText().toString().trim(),new EMCallBack() {//回调
                            @Override
                            public void onSuccess() {
                                EMClient.getInstance().groupManager().loadAllGroups();
                                EMClient.getInstance().chatManager().loadAllConversations();
                                if (App.isMain){
                                    App.get().getSp().putString(Constant.TOKEN, response.body().data.getToken());
                                }else {
                                    App.get().getSp().putString(Constant.TEST_TOKEN, response.body().data.getToken());
                                }
                                App.get().getSp().putString(Constant.USER_INFO, GsonUtils.objToJson(response.body().data));
                                App.get().setLoginBean(response.body().data);
                                App.get().setLogin(true);
                                App.get().getSp().putBoolean(Constant.NEED_RESTART,true);
                                App.get().getSp().putString(Constant.LOGIN_NAME+App.isMain,email.getText().toString().trim());

//                                boolean needUpdate=false;
//                                if (AppUtil.getVersion(mActivity)>App.get().getSp().getInt(Constant.VERSION,0)){
//                                    needUpdate=true;
//                                    App.get().getSp().putInt(Constant.VERSION,AppUtil.getVersion(mActivity));
//                                }
                                ArrayList<CommonProjectBean> mainCacheMarks= CacheUtils.getCache(Constant.PROJECT_JSON_MAIN+(null==App.get().getLoginBean()?"":App.get().getLoginBean().getEmail()));
                                ArrayList<CommonProjectBean> testCacheMarks= CacheUtils.getCache(Constant.PROJECT_JSON_TEST+(null==App.get().getLoginBean()?"":App.get().getLoginBean().getEmail()));
                                ArrayList<CommonProjectBean> marks=new ArrayList<>();
                                if (null==mainCacheMarks||null==testCacheMarks){
                                    marks=GsonUtils.jsonToArrayList(Constant.BASE_PROJECT_JSON, CommonProjectBean.class);
                                }
                                if (null==mainCacheMarks){
                                    CacheUtils.setCache(Constant.PROJECT_JSON_MAIN+(null==App.get().getLoginBean()?"":App.get().getLoginBean().getEmail()), marks);
                                }
                                if (null==testCacheMarks){
                                    CacheUtils.setCache(Constant.PROJECT_JSON_TEST+(null==App.get().getLoginBean()?"":App.get().getLoginBean().getEmail()), marks);
                                }

                                if (null!=response.body().data.getWallet_gnt_sort()){
                                    final HashMap<String,Integer> sort=new HashMap<>();
                                    //获取排序
                                    for (int i=0;i<response.body().data.getWallet_gnt_sort().size();i++){
                                        sort.put(response.body().data.getWallet_gnt_sort().get(i),i);
                                    }
                                    CacheUtils.setCache(Constant.SORT+App.isMain,sort);
                                }

                                boolean isZh=true;
                                if (response.body().data.getLang().equals("zh")){
                                    isZh=true;
                                }else {
                                    isZh=false;
                                }

                                if (App.get().isZh()!=isZh){
                                    LocaleUtils.updateLocale(mActivity, LocaleUtils.LOCALE_ENGLISH);
                                    App.get().getSp().putBoolean(Constant.IS_CHINESE,isZh);
                                    App.get().getSp().putInt(Constant.UNIT_TYPE,isZh?1:2);
                                    //设置语言
                                    AppUtil.changeAppLanguage(mActivity);
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideFixLoading();
                                        Intent intent=new Intent(mActivity,MainTabActivity.class);
                                        finshTogo(intent);
                                    }
                                });
                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }

                            @Override
                            public void onError(int code, final String message) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideFixLoading();
                                        ToastUtil.show(getString(R.string.huanxingdenglushibai)+message);
                                    }
                                });
                            }

                        });
                    }

                    @Override
                    public void onError(Response<LzyResponse<LoginBean>> response) {
                        super.onError(response);
                        hideFixLoading();
                        if (null!=response.getException().getMessage()){
                            if (response.getException().getMessage().contains("4004")){
                                ToastUtil.show(getString(R.string.xuandingdeemailshiwuxiaode));
                                return;
                            }else if (response.getException().getMessage().contains("5201")){
                                ToastUtil.show(getString(R.string.zhanghuhuomimacuowu));
                                return;
                            }
                        }
                        ToastUtil.show(getString(R.string.denglushibai));
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //1.点击返回键条件成立
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            if (isFixLoadingShow()) {
                hideFixLoading();
                OkGo.getInstance().cancelTag(this);
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
