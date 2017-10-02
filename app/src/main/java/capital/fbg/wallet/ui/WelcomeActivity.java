package capital.fbg.wallet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Locale;

import butterknife.BindView;
import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.event.BaseEventBusBean;
import capital.fbg.wallet.ui.login.LoginActivity;

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
        ((ViewGroup)findViewById(android.R.id.content)).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppApplication.get().getSp().getBoolean(Constant.IS_CLOD,false)){
                    finshTogo(MainTabActivity.class);
                }else {
                    if (null==AppApplication.get().getSp().getString(Constant.TOKEN)||"".equals(AppApplication.get().getSp().getString(Constant.TOKEN))){
                        Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
                        //如果启动app的Intent中带有额外的参数，表明app是从点击通知栏的动作中启动的
                        //将参数取出，传递到MainActivity中
                        if(getIntent().getStringExtra("pushInfo") != null){
                            intent.putExtra("pushInfo",
                                    getIntent().getStringExtra("pushInfo"));
                        }
                        finshTogo(intent);
                    }else {
                        Intent intent=new Intent(WelcomeActivity.this,MainTabActivity.class);
                        //如果启动app的Intent中带有额外的参数，表明app是从点击通知栏的动作中启动的
                        //将参数取出，传递到MainActivity中
                        if(getIntent().getStringExtra("pushInfo") != null){
                            intent.putExtra("pushInfo",
                                    getIntent().getStringExtra("pushInfo"));
                        }
                        finshTogo(intent);
                    }
                }
            }
        },2000);
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
