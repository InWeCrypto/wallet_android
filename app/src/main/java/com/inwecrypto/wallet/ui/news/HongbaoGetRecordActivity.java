package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonPageBean;
import com.inwecrypto.wallet.bean.DrawRecordBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.adapter.GetHongbaoAdapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/4/23 16:03
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class HongbaoGetRecordActivity extends BaseActivity {
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

    private ArrayList<DrawRecordBean> data=new ArrayList<>();
    private GetHongbaoAdapter adapter;

    private ArrayList<WalletBean> wallet = new ArrayList<>();

    private int page=1;
    private boolean isEnd;
    private boolean isInit;

    private LoadMoreWrapper loadMoreWrapper;
    private EmptyWrapper emptyWrapper;
    private View loadMore;

    @Override
    protected void getBundleExtras(Bundle extras) {
        wallet= (ArrayList<WalletBean>) extras.getSerializable("wallet");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.hongbao_get_record_activity;
    }

    @Override
    protected void initView() {

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(R.string.lingqujilu);
        txtRightTitle.setVisibility(View.GONE);


        adapter=new GetHongbaoAdapter(this,R.layout.get_hongbao_record_adapter,data);
        emptyWrapper=new EmptyWrapper(adapter);
        emptyWrapper.setEmptyView(R.layout.empty_list_layout);
        loadMoreWrapper=new LoadMoreWrapper(emptyWrapper);
        loadMore= LayoutInflater.from(this).inflate(R.layout.load_more_layout,null,false);
        loadMore.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        loadMoreWrapper.setLoadMoreView(loadMore);
        loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (isEnd||!isInit){
                    return;
                }
                page++;
                ((TextView)loadMore.findViewById(R.id.load_more)).setText(R.string.jiazaizhong);
                initData();
            }
        });
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(loadMoreWrapper);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                isEnd=false;
                ((TextView)loadMore.findViewById(R.id.load_more)).setText("");
                initData();
            }
        });

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,HongbaoGetRecordDetailActivity.class);
                intent.putExtra("redbag",data.get(position));
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
        ZixunApi.getDrawRecord(this, wallet, page, new JsonCallback<LzyResponse<CommonPageBean<DrawRecordBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonPageBean<DrawRecordBean>>> response) {
                setSuccess(response.body().data);
            }

            @Override
            public void onError(Response<LzyResponse<CommonPageBean<DrawRecordBean>>> response) {
                super.onError(response);
                if (page!=1){
                    page--;
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void setSuccess(CommonPageBean<DrawRecordBean> response) {

        if (null==swipeRefresh)return;

        if (response.getCurrent_page()==1){
            data.clear();
        }

        //最后一页
        if (response.getCurrent_page()==response.getLast_page()){
            isEnd=true;
            if (data.size()!=0){
                ((TextView)loadMore.findViewById(R.id.load_more)).setText(R.string.zanwugengduoshuju);
            }
        }

        if (null!=response.getData()){
            data.addAll(response.getData());
        }

        loadMoreWrapper.notifyDataSetChanged();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
