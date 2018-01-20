package com.inwecrypto.wallet.ui.newneo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
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
import com.inwecrypto.wallet.ui.wallet.adapter.NeoWalletListAdapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/1/8 11:08
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NewNeoWalletListActivity extends BaseActivity {

    @BindView(R.id.wallet_list)
    RecyclerView walletList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.left)
    FrameLayout left;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.right)
    FrameLayout right;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;

    private NeoWalletListAdapter adapter;
    private ArrayList<WalletBean> wallet = new ArrayList<>();

    private int type_id;

    @Override
    protected void getBundleExtras(Bundle extras) {
        wallet.addAll((ArrayList<WalletBean>) extras.getSerializable("wallet"));
    }

    @Override
    protected int setLayoutID() {
        return R.layout.newneo_wallet_list_activity;
    }

    @Override
    protected void initView() {

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                CreateWalletFragment create = new CreateWalletFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("wallets",wallet);
                create.setArguments(bundle);
                create.show(fm,"create");
            }
        });

        adapter = new NeoWalletListAdapter(this, R.layout.newneo_wallet_list_item, wallet);
        walletList.setLayoutManager(new LinearLayoutManager(this));
        walletList.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, final int position) {
                Intent intent = new Intent(mActivity, NewNeoWalletActivity.class);
                intent.putExtra("wallet", wallet.get(position));
                finshTogo(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //从网络获取钱包
                getWalletOnNet();
            }
        });
    }

    @Override
    protected void initData() {

    }

    private void getWalletOnNet() {
        WalletApi.wallet(mActivity, new JsonCallback<LzyResponse<CommonListBean<WalletBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                wallet.clear();
                if (null != response.body().data.getList()) {
                    String wallets = App.get().getSp().getString(Constant.WALLETS, "");
                    String wallets_beifen = App.get().getSp().getString(Constant.WALLETS_BEIFEN, "");
                    String walletsZjc = App.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "");
                    for (int i = 0; i < response.body().data.getList().size(); i++) {
                        if (response.body().data.getList().get(i).getCategory_id() == 2) {
                            if (wallets.contains(response.body().data.getList().get(i).getAddress())) {
                                if (wallets_beifen.contains(response.body().data.getList().get(i).getAddress()) || walletsZjc.contains(response.body().data.getList().get(i).getAddress())) {
                                    response.body().data.getList().get(i).setType(Constant.BEIFEN);
                                } else {
                                    response.body().data.getList().get(i).setType(Constant.ZHENGCHANG);
                                }
                            } else {
                                response.body().data.getList().get(i).setType(Constant.GUANCHA);
                            }
                            wallet.add(response.body().data.getList().get(i));
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                super.onError(response);
                ToastUtil.show(R.string.jiazaishibai);
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_MAIN_REFERSH_COMP));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (null!=swipeRefresh){
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_WALLET||event.getEventCode() == Constant.EVENT_WALLET_DAIBI
                || event.getEventCode() == Constant.EVENT_UNIT_CHANGE) {
            swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    if (null!=swipeRefresh){
                        swipeRefresh.setRefreshing(true);
                    }
                }
            });
            getWalletOnNet();
        }
    }

}
