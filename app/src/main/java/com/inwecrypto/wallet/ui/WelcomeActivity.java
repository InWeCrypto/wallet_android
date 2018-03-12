package com.inwecrypto.wallet.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonProjectBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.CacheUtils;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import com.inwecrypto.wallet.ui.newneo.WalletFragment;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Administrator on 2017/7/14.
 * 功能描述：
 * 版本：@version
 */

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void getBundleExtras(Bundle extras) {
    }

    @Override
    protected int setLayoutID() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
            findViewById(android.R.id.content).postDelayed(new Runnable() {
                @Override
                public void run() {
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
                    Intent intent = new Intent(WelcomeActivity.this, MainTabActivity.class);
                    //如果启动app的Intent中带有额外的参数，表明app是从点击通知栏的动作中启动的
                    //将参数取出，传递到MainActivity中
                    if (getIntent().getStringExtra("pushInfo") != null) {
                        intent.putExtra("pushInfo",
                                getIntent().getStringExtra("pushInfo"));
                    }
                    finshTogo(intent);
                }
            }, 2000);
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
