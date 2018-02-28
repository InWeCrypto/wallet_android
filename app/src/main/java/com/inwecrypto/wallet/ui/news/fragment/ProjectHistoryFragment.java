package com.inwecrypto.wallet.ui.news.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.ArticleDetaileBean;
import com.inwecrypto.wallet.bean.ArticleListBean;
import com.inwecrypto.wallet.bean.ExchangeNoticeBean;
import com.inwecrypto.wallet.bean.ExchangeNoticeListBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.EndLessOnScrollListener;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.ProjectNewsWebActivity;
import com.inwecrypto.wallet.ui.news.adapter.ExchangeNoticeAdapater;
import com.inwecrypto.wallet.ui.news.adapter.InweHotHistoryAdapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * 作者：xiaoji06 on 2018/2/9 17:07
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectHistoryFragment extends BaseFragment {
    @BindView(R.id.mail_list)
    RecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private int type=-1;
    private int id;

    private int page = 0;
    private boolean isEnd;
    private boolean isShow;

    private LinearLayoutManager layoutManager;

    private InweHotHistoryAdapter adapter;
    private ArrayList<ArticleDetaileBean> data=new ArrayList<>();

    @Override
    protected int setLayoutID() {
        return R.layout.me_fragment_mail_list;
    }

    @Override
    protected void initView() {

        type=getArguments().getInt("type",-1);
        id=getArguments().getInt("id");

        adapter = new InweHotHistoryAdapter(mContext, R.layout.shoucang_item, data);
        layoutManager = new LinearLayoutManager(mContext);
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
                    loadData();
                }
            }
        });

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isEnd = false;
                loadData();
            }
        });
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,ProjectNewsWebActivity.class);
                intent.putExtra("title",data.get(position).getTitle());
                intent.putExtra("url", (App.isMain? Url.MAIN_NEWS:Url.TEST_NEWS)+data.get(position).getId());
                intent.putExtra("id",data.get(position).getId());
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
    protected void loadData() {
        ZixunApi.getHistoryNews(this, id, type, page, new JsonCallback<LzyResponse<ArticleListBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArticleListBean>> response) {
                LoadSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<ArticleListBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                if (page!=1){
                    page--;
                }
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

    private void LoadSuccess(final Response<LzyResponse<ArticleListBean>> response) {
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
        if (null!=response.body().data.getData()){
            data.addAll(response.body().data.getData());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
