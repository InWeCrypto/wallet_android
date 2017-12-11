package com.inwecrypto.wallet.ui.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.wallet.activity.neowallet.WatchImportNeoWalletActivity;

/**
 * Created by Administrator on 2017/8/9.
 * 功能描述：
 * 版本：@version
 */

public class WatchImportWalletTypeActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.keystore)
    LinearLayout keystore;
    @BindView(R.id.anquanma)
    LinearLayout anquanma;
    @BindView(R.id.key)
    LinearLayout key;
    @BindView(R.id.watch)
    LinearLayout watch;
    @BindView(R.id.seed)
    LinearLayout seed;

    private WalletBean wallet;

    @Override
    protected void getBundleExtras(Bundle extras) {
        wallet= (WalletBean) extras.getSerializable("wallet");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_acivity_import_wallet_type;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.zhuanhuaqianbao);
        txtRightTitle.setVisibility(View.GONE);
        keystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoImprot(1);
            }
        });
        anquanma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoImprot(2);
            }
        });
        key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoImprot(3);
            }
        });
        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoImprot(4);
            }
        });
        seed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoImprot(5);
            }
        });
    }

    private void gotoImprot(int type) {
        Intent intent=null;
        if (wallet.getCategory_id()==2){
            intent=new Intent(mActivity,WatchImportNeoWalletActivity.class);
        }else {
            intent=new Intent(mActivity,WatchImportWalletActivity.class);
        }
        intent.putExtra("type",type);
        intent.putExtra("wallet",wallet);
        keepTogo(intent);
    }

    @Override
    protected void initData() {
        watch.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
