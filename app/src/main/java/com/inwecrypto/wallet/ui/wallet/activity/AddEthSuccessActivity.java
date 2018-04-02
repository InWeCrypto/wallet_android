package com.inwecrypto.wallet.ui.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.newneo.NewNeoWalletListActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class AddEthSuccessActivity extends BaseActivity {


    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_beifen)
    TextView tvBeifen;
    @BindView(R.id.gotoWallet)
    TextView gotoWallet;
    private WalletBean wallet;
    private String zjc;

    @Override
    protected void getBundleExtras(Bundle extras) {
        wallet = (WalletBean) extras.getSerializable("wallet");
        zjc=extras.getString("zjc");
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
        img.setImageResource(R.mipmap.s_eth_walllet);
        tvBeifen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, WalletTipOneActivity.class);
                intent.putExtra("zjc", zjc);
                intent.putExtra("wallet", wallet);
                intent.putExtra("isNew",true);
                intent.putExtra("isEth",true);
                keepTogo(intent);
            }
        });
        gotoWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoWallet();
            }
        });
    }

    private void gotoWallet() {
        AppManager.getAppManager().finishActivity(NewNeoWalletListActivity.class);
        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
        Intent intent = new Intent(mActivity, HotWalletActivity.class);
        intent.putExtra("wallet", wallet);
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
