package com.inwecrypto.wallet.ui.me.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/2/7 18:20
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.unit)
    TextView unit;
    @BindView(R.id.b2)
    ImageView b2;
    @BindView(R.id.unitll)
    RelativeLayout unitll;
    @BindView(R.id.net)
    TextView net;
    @BindView(R.id.b3)
    ImageView b3;
    @BindView(R.id.netll)
    RelativeLayout netll;
    @BindView(R.id.langue)
    TextView langue;
    @BindView(R.id.b4)
    ImageView b4;
    @BindView(R.id.languell)
    RelativeLayout languell;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.setting_activity;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(R.string.shezhi);

        txtRightTitle.setVisibility(View.GONE);

        unitll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,SettingTypeActivity.class);
                intent.putExtra("type",1);
                keepTogo(intent);
            }
        });

        netll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,SettingTypeActivity.class);
                intent.putExtra("type",2);
                keepTogo(intent);
            }
        });

        languell.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,SettingTypeActivity.class);
                intent.putExtra("type",3);
                keepTogo(intent);
            }
        });
    }

    @Override
    protected void initData() {

        unit.setText(App.get().getUnit()==1?getString(R.string.renmingbi):getString(R.string.meiyuan));
        net.setText(App.isMain?getString(R.string.zhengshiwangluo):getString(R.string.ceshiwangluo));
        langue.setText(App.get().isZh()?getString(R.string.jiantizhongwen):getString(R.string.yingwen));
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()== Constant.EVENT_UNIT_CHANGE){
            initData();
        }
    }
}
