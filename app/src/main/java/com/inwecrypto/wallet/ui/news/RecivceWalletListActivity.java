package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.newneo.NewNeoWalletActivity;
import com.inwecrypto.wallet.ui.wallet.activity.HotWalletActivity;
import com.inwecrypto.wallet.ui.wallet.adapter.NeoWalletListAdapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/1/8 11:08
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class RecivceWalletListActivity extends BaseActivity {


    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.wallet_list)
    RecyclerView walletList;
    private NeoWalletListAdapter adapter;
    private ArrayList<WalletBean> wallet=new ArrayList<>();

    @Override
    protected void getBundleExtras(Bundle extras) {
        wallet.addAll((ArrayList<WalletBean>) extras.getSerializable("wallet"));
    }

    @Override
    protected int setLayoutID() {
        return R.layout.recivce_wallet_list_activity;
    }

    @Override
    protected void initView() {

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.qianbaoliebiao);
        txtRightTitle.setVisibility(View.GONE);

        adapter = new NeoWalletListAdapter(this, R.layout.newneo_wallet_list_item, wallet);
        walletList.setLayoutManager(new LinearLayoutManager(this));
        walletList.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, final int position) {
                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET_SELECT,position));
                    finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    @Override
    protected void initData() {

    }

    private void getWalletOnNet() {
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
    }

}
