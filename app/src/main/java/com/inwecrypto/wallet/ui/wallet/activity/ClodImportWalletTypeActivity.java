package com.inwecrypto.wallet.ui.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

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

public class ClodImportWalletTypeActivity extends BaseActivity {
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

    private int type_id;

    private ArrayList<WalletBean> wallets;

    @Override
    protected void getBundleExtras(Bundle extras) {
        type_id=extras.getInt("type_id");
        wallets= (ArrayList<WalletBean>) extras.getSerializable("wallets");
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
        txtMainTitle.setText(R.string.import_wallet);
        txtRightTitle.setVisibility(View.GONE);
        keystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,ClodImportWalletActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("type_id",type_id);
                intent.putExtra("wallets",wallets);
                keepTogo(intent);
            }
        });
        anquanma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,ClodImportWalletActivity.class);
                intent.putExtra("type",2);
                intent.putExtra("type_id",type_id);
                intent.putExtra("wallets",wallets);
                keepTogo(intent);
            }
        });
        key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,ClodImportWalletActivity.class);
                intent.putExtra("type",3);
                intent.putExtra("type_id",type_id);
                intent.putExtra("wallets",wallets);
                keepTogo(intent);
            }
        });
        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        seed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,ClodImportWalletActivity.class);
                intent.putExtra("type",5);
                intent.putExtra("type_id",type_id);
                keepTogo(intent);
            }
        });
        watch.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
