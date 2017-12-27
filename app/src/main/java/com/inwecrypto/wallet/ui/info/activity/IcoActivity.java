package com.inwecrypto.wallet.ui.info.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.IcoBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.InfoApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.info.adapter.IcoAdapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2017/11/13 17:14
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class IcoActivity extends BaseActivity {


    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.serch)
    FrameLayout serch;
    @BindView(R.id.menu)
    FrameLayout menu;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private ArrayList<IcoBean> icoBeans = new ArrayList<>();
    private IcoAdapter adapter;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.info_activity_ico_layout;
    }

    @Override
    protected void initView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.showMainMenu(menu, IcoActivity.this, true);
            }
        });

        serch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SearchActivity.class);
                intent.putExtra("type", 2);
                keepTogo(intent);
            }
        });

        title.setText(R.string.icopingce);

        state.setVisibility(View.GONE);

        adapter = new IcoAdapter(this, R.layout.info_item_ico, icoBeans);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (null == icoBeans || icoBeans.size() == 0) {
                    return;
                }
                if (null != icoBeans.get(position).getUrl()) {
                    Intent intent = new Intent(mActivity, InfoWebActivity.class);
                    intent.putExtra("title", icoBeans.get(position).getTitle());
                    intent.putExtra("url", icoBeans.get(position).getUrl().startsWith("http") ? icoBeans.get(position).getUrl() : Url.WEB_ROOT + icoBeans.get(position).getUrl().replace("../", ""));
                    keepTogo(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
        InfoApi.getICONews(this, new JsonCallback<LzyResponse<ArrayList<IcoBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<IcoBean>>> response) {
                swipeRefresh.setRefreshing(false);
                if (null != response) {
                    if (null != response.body().data) {
                        icoBeans.clear();
                        icoBeans.addAll(response.body().data);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(Response<LzyResponse<ArrayList<IcoBean>>> response) {
                super.onError(response);
                swipeRefresh.setRefreshing(false);
                ToastUtil.show(R.string.load_error);
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
