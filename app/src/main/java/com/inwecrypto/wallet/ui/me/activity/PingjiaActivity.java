package com.inwecrypto.wallet.ui.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.PingjiaBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MeApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.EndLessOnScrollListener;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.adapter.MarketTipAdapter;
import com.inwecrypto.wallet.ui.me.adapter.PingjiaAdapter;
import com.lzy.okgo.model.Response;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/4/3 17:44
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class PingjiaActivity extends BaseActivity {
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
    SwipeRefreshLayout swipeRefresh;

    private LinearLayoutManager layoutManager;

    private PingjiaAdapter adapter;
    private ArrayList<PingjiaBean> data=new ArrayList<>();

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.pingjia_activity_layout;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(R.string.wopinglundexiangmu);

        txtRightTitle.setVisibility(View.GONE);

        adapter = new PingjiaAdapter(this, R.layout.pinglun_item_layout, data);
        layoutManager=new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //从网络获取钱包
                initData();
            }
        });

        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,MyPingjiaActivity.class);
                intent.putExtra("pingjia",data.get(position));
                intent.putExtra("isFromMe",true);
                keepTogo(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        MeApi.getComment(this, new JsonCallback<LzyResponse<ArrayList<PingjiaBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<PingjiaBean>>> response) {
                data.clear();
                data.addAll(response.body().data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (null==swipeRefresh)return;
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
