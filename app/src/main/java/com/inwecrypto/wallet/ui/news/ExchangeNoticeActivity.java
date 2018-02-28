package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonProjectBean;
import com.inwecrypto.wallet.bean.ExchangeNoticeBean;
import com.inwecrypto.wallet.bean.ExchangeNoticeListBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.adapter.ExchangeNoticeAdapater;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/2/8 20:01
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ExchangeNoticeActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    ImageView txtRightTitle;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.views)
    TextView views;

    private int page=1;
    private boolean isEnd;

    private CommonProjectBean marks;

    private ExchangeNoticeAdapater adapter;
    private ArrayList<ExchangeNoticeBean> data=new ArrayList<>();

    private boolean isFirst;

    @Override
    protected void getBundleExtras(Bundle extras) {
        marks= (CommonProjectBean) extras.getSerializable("marks");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.inweproject_activity;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.jiaoyisuogonggao);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,ProjectDetaileActivity.class);
                intent.putExtra("marks",marks);
                keepTogo(intent);
            }
        });

        adapter=new ExchangeNoticeAdapater(this,R.layout.jiaoyigonggao_item,data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        list.setLayoutManager(linearLayoutManager);
        list.setAdapter(adapter);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isFirst){
                    page++;
                }else {
                    page=1;
                }
                isEnd=false;
                initData();
            }
        });
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
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
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        ZixunApi.getExchangeNotice(this, page, new JsonCallback<LzyResponse<ExchangeNoticeListBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ExchangeNoticeListBean>> response) {
                if (!response.isFromCache()){
                    if (!isFirst){
                        isFirst=true;
                    }
                }
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
                swipeRefresh.setEnabled(false);
            }
        }else {
            if (page>=response.body().data.getLast_page()){
                isEnd=true;
                swipeRefresh.setEnabled(false);
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
                        data.add(0,notice);
                    }
                }
                final int finalCount = count;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemRangeInserted(0, finalCount);
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
