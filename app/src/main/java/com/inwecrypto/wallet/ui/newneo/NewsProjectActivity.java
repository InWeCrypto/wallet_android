package com.inwecrypto.wallet.ui.newneo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.ProjectDetaileBean;
import com.inwecrypto.wallet.bean.ProjectListBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.NoTradingActivity;
import com.inwecrypto.wallet.ui.news.TradingActivity;
import com.inwecrypto.wallet.ui.news.adapter.ProjectAdatpter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/3/14 17:33
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NewsProjectActivity extends BaseActivity {

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

    private NewsProjectAdapter adapter;
    private ArrayList<ProjectDetaileBean> data=new ArrayList<>();

    private String projects="";

    @Override
    protected void getBundleExtras(Bundle extras) {
        projects=extras.getString("projects");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.news_project_activity;
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

        txtMainTitle.setText(R.string.xiangmuzixunchakan);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        adapter=new NewsProjectAdapter(this,R.layout.news_project_item ,data);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (1==data.get(position).getType()){
                    Intent intent=new Intent(mActivity, TradingActivity.class);
                    intent.putExtra("project",data.get(position));
                    intent.putExtra("type",-1);
                    keepTogo(intent);
                }else {
                    Intent intent=new Intent(mActivity, NoTradingActivity.class);
                    intent.putExtra("project",data.get(position));
                    intent.putExtra("type",-1);
                    keepTogo(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
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
        ZixunApi.getFixProject(this, projects, new JsonCallback<LzyResponse<ProjectListBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ProjectListBean>> response) {
                data.clear();
                if (null!=response.body().data&&null!=response.body().data.getData()){
                    data.addAll(response.body().data.getData());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
