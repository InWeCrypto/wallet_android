package com.inwecrypto.wallet.ui.news;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.ProjectDetaileBean;
import com.inwecrypto.wallet.bean.ProjectMarketBean;
import com.inwecrypto.wallet.bean.TradingProjectDetaileBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.adapter.TradingMarketAdapter;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/2/10 11:29
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class TradingMarketActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;

    private LinearLayoutManager layoutManager;

    private TradingMarketAdapter adapter;

    private ArrayList<ProjectMarketBean> data=new ArrayList<>();

    private TradingProjectDetaileBean project;

    @Override
    protected void getBundleExtras(Bundle extras) {
        project= (TradingProjectDetaileBean) extras.getSerializable("project");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.inwehot_history_activity;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtRightTitle.setVisibility(View.GONE);

        txtMainTitle.setText(project.getUnit());

        adapter = new TradingMarketAdapter(this, R.layout.trading_market_item, data);
        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
    }


    @Override
    protected void initData() {
        ZixunApi.getMarkets(this, project.getUnit(), new JsonCallback<LzyResponse<ArrayList<ProjectMarketBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<ProjectMarketBean>>> response) {
                data.clear();
                if (null!=response.body().data){
                    data.addAll(response.body().data);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response<LzyResponse<ArrayList<ProjectMarketBean>>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (null != swipeRefresh) {
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
    }


    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
