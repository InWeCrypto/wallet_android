package com.inwecrypto.wallet.ui.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class ClodAddEthSuccessActivity extends BaseActivity {
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.tv_go)
    TextView tvGo;

    private String  address;
    private int icon;

    @Override
    protected void getBundleExtras(Bundle extras) {
        address= extras.getString("address");
        icon=extras.getInt("icon");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_acivity_add_eth_success;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(getString(R.string.add_wallet_title));
        txtRightTitle.setVisibility(View.GONE);
        tvGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getAppManager().finishActivity(ClodAddWalletSettingActivity.class);
                AppManager.getAppManager().finishActivity(ClodAddWalletListActivity.class);
                EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_WALLET));
                Intent intent=new Intent(mActivity,ColdWalletActivity.class);
                intent.putExtra("address",address);
                intent.putExtra("icon",icon);
                finshTogo(intent);
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
