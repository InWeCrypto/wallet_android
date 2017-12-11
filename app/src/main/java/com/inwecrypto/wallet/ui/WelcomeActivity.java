package com.inwecrypto.wallet.ui;

import android.content.Intent;
import android.os.Bundle;

import com.inwecrypto.wallet.AppApplication;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.login.LoginActivity;

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
                if (null == AppApplication.get().getSp().getString(AppApplication.isMain?Constant.TOKEN:Constant.TEST_TOKEN) || "".equals(AppApplication.get().getSp().getString(AppApplication.isMain?Constant.TOKEN:Constant.TEST_TOKEN))) {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    //如果启动app的Intent中带有额外的参数，表明app是从点击通知栏的动作中启动的
                    //将参数取出，传递到MainActivity中
                    if (getIntent().getStringExtra("pushInfo") != null) {
                        intent.putExtra("pushInfo",
                                getIntent().getStringExtra("pushInfo"));
                    }
                    finshTogo(intent);
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, MainTabActivity.class);
                    //如果启动app的Intent中带有额外的参数，表明app是从点击通知栏的动作中启动的
                    //将参数取出，传递到MainActivity中
                    if (getIntent().getStringExtra("pushInfo") != null) {
                        intent.putExtra("pushInfo",
                                getIntent().getStringExtra("pushInfo"));
                    }
                    finshTogo(intent);
                }
            }
        }, 2000);
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
