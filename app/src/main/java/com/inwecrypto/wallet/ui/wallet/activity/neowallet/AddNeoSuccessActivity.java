package com.inwecrypto.wallet.ui.wallet.activity.neowallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.wallet.activity.AddEthWalletActivity;
import com.inwecrypto.wallet.ui.wallet.activity.AddWalletListActivity;
import com.inwecrypto.wallet.ui.wallet.activity.AddWalletSettingActivity;
import com.inwecrypto.wallet.ui.wallet.activity.AddWalletTypeActivity;
import com.inwecrypto.wallet.ui.wallet.activity.HotWalletActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class AddNeoSuccessActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.tv_go)
    TextView tvGo;

    private WalletBean wallet;

    @Override
    protected void getBundleExtras(Bundle extras) {
        wallet= (WalletBean) extras.getSerializable("wallet");
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
                gotoWallet();
            }
        });
        txtMainTitle.setText(R.string.tianjiaqianbao);
        txtRightTitle.setVisibility(View.GONE);
        tvGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoWallet();
            }
        });
    }
    private void gotoWallet() {
        AppManager.getAppManager().finishActivity(AddWalletTypeActivity.class);
        AppManager.getAppManager().finishActivity(AddWalletListActivity.class);
        EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_WALLET));
        Intent intent=new Intent(mActivity,NeoWalletActivity.class);
        intent.putExtra("wallet",wallet);
        finshTogo(intent);
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
            gotoWallet();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
