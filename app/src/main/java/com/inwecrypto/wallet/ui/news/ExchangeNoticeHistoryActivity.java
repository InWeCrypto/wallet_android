package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.ArticleDetaileBean;
import com.inwecrypto.wallet.bean.ArticleListBean;
import com.inwecrypto.wallet.bean.ExchangeNoticeBean;
import com.inwecrypto.wallet.bean.ExchangeNoticeListBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.EndLessOnScrollListener;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.adapter.ExchangeNoticeAdapater;
import com.inwecrypto.wallet.ui.news.adapter.InweHotHistoryAdapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/2/10 11:29
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ExchangeNoticeHistoryActivity extends BaseActivity {

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

    private int page = 1;
    private boolean isEnd;
    private boolean isShow;

    private LinearLayoutManager layoutManager;

    private ExchangeNoticeAdapater adapter;
    private ArrayList<ExchangeNoticeBean> data=new ArrayList<>();

    @Override
    protected void getBundleExtras(Bundle extras) {
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

        txtMainTitle.setText(R.string.lishizixun);

        adapter = new ExchangeNoticeAdapater(this, R.layout.exchange_notice_history_item, data);
        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
        list.addOnScrollListener(new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                if (isEnd) {
                    if (!isShow && page != 1) {
                        ToastUtil.show(getString(R.string.zanwugengduoshuju));
                        isShow = true;
                    }
                } else {
                    page++;
                    initData();
                }
            }
        });

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isEnd = false;
                initData();
            }
        });
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,ProjectNewsWebActivity.class);
                intent.putExtra("title",data.get(position).getSource_name());
                intent.putExtra("url", data.get(position).getSource_url());
                intent.putExtra("show",false);
                keepTogo(intent);
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
        ZixunApi.getExchangeNotice(this, page, new JsonCallback<LzyResponse<ExchangeNoticeListBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ExchangeNoticeListBean>> response) {
                LoadSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<ExchangeNoticeListBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                if (page!=1){
                    page--;
                }
            }
        });
    }

    private void LoadSuccess(final Response<LzyResponse<ExchangeNoticeListBean>> response) {
        if (page==1){
            data.clear();
            if (1>=response.body().data.getLast_page()){
                isEnd=true;
            }
        }else {
            if (page>=response.body().data.getLast_page()){
                isEnd=true;
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                int count=0;
                if (null!=response.body().data.getData()){
                    count=response.body().data.getData().size();
                    for (int i=0;i<count;i++){
                        ExchangeNoticeBean notice = response.body().data.getData().get(i);
                        notice.setContent(AppUtil.delHTMLTag(notice.getContent()));
                        data.add(notice);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        if (null != swipeRefresh) {
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
