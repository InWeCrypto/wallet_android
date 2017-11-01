package com.inwecrypto.wallet.ui.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;


import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.TransferBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.ui.ScanActivity;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.KeyEvent;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2017/7/16.
 * 功能描述：
 * 版本：@version
 */

public class ClodTokenWalletActivity extends BaseActivity {


    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_ch_price)
    TextView tvChPrice;
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.wallet_list)
    SwipeMenuRecyclerView walletList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.ll_zhuanzhang)
    LinearLayout llZhuanzhang;
    @BindView(R.id.ll_shoukuan)
    LinearLayout llShoukuan;

    private String address;
    private MaterialDialog mMaterialDialog;
    private TransferBean transfer;


    @Override
    protected void getBundleExtras(Bundle extras) {
        isOpenEventBus = true;
        address = extras.getString("address");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_acivity_clod_token_new;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText("ETH");
        txtRightTitle.setVisibility(View.GONE);

        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    swipeRefresh.setEnabled(true);
                } else {
                    swipeRefresh.setEnabled(false);
                }
            }
        });
        llZhuanzhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isConnected(mActivity)) {
                    ToastUtil.show("请确保手机处于飞行模式！");
                    return;
                } else {
                    Intent intent=new Intent(mActivity,ScanActivity.class);
                    intent.putExtra("title","扫码签名");
                    keepTogo(intent);
                }
            }
        });

        llShoukuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ClodReceiveActivity.class);
                intent.putExtra("address", address);
                keepTogo(intent);
            }
        });

        Glide.with(this).load(R.mipmap.eth).crossFade().into(ivImg);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_KEY) {
            KeyEvent key = (KeyEvent) event.getData();
            getData(key.getKey());
        }
    }

    private void getData(String key) {
        if (null != key && key.length() == 0) {
            ToastUtil.show("请重新扫描二维码");
            return;
        }

        try {
            Gson gson = new Gson();
            transfer = gson.fromJson(key, TransferBean.class);
        } catch (Exception e) {
            ToastUtil.show("扫描数据不正确");
            return;
        }

        if (!transfer.getWallet_address().toLowerCase().equals(address.toLowerCase())) {
            ToastUtil.show("钱包地址不相同，请使用相同的钱包地址进行扫描");
            return;
        }

        Intent intent = new Intent(this, ClodTransferAccountsConfirmActivity.class);
        intent.putExtra("transfer", transfer);
        keepTogo(intent);
    }

}
