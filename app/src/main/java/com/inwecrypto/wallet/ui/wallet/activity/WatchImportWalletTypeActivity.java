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
        txtMainTitle.setText("转化钱包");
        txtRightTitle.setVisibility(View.GONE);
        keystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,WatchImportWalletActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("wallet",wallet);
                keepTogo(intent);
            }
        });
        anquanma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,WatchImportWalletActivity.class);
                intent.putExtra("type",2);
                intent.putExtra("wallet",wallet);
                keepTogo(intent);
            }
        });
        key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,WatchImportWalletActivity.class);
                intent.putExtra("type",3);
                intent.putExtra("wallet",wallet);
                keepTogo(intent);
            }
        });
        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,WatchImportWalletActivity.class);
                intent.putExtra("type",4);
                intent.putExtra("wallet",wallet);
                keepTogo(intent);
            }
        });
        seed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,WatchImportWalletActivity.class);
                intent.putExtra("type",5);
                intent.putExtra("wallet",wallet);
                keepTogo(intent);
            }
        });
    }

    @Override
    protected void initData() {
        watch.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
