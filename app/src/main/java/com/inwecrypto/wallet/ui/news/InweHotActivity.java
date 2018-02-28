package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.ArticleDetaileBean;
import com.inwecrypto.wallet.bean.ArticleListBean;
import com.inwecrypto.wallet.bean.CommonProjectBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.adapter.InwehotAdapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/2/8 20:01
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class InweHotActivity extends BaseActivity {


    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    ImageView txtRightTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.hot)
    TextView hot;
    @BindView(R.id.h24)
    TextView h24;
    @BindView(R.id.views)
    TextView views;
    private int page = 1;
    private boolean isEnd;

    private CommonProjectBean marks;

    private InwehotAdapter adapter;
    private ArrayList<ArticleDetaileBean> data = new ArrayList<>();

    private boolean isFirst;

    @Override
    protected void getBundleExtras(Bundle extras) {
        marks = (CommonProjectBean) extras.getSerializable("marks");
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
        txtMainTitle.setText(R.string.inweerdian);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ProjectDetaileActivity.class);
                intent.putExtra("marks", marks);
                keepTogo(intent);
            }
        });

        adapter = new InwehotAdapter(this, R.layout.inwe_hot_item, data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        list.setLayoutManager(linearLayoutManager);
        list.setAdapter(adapter);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isFirst) {
                    page++;
                } else {
                    page = 1;
                }
                isEnd = false;
                initData();
            }
        });

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mActivity, ProjectNewsWebActivity.class);
                intent.putExtra("title", data.get(position).getTitle());
                intent.putExtra("url", (App.isMain ? Url.MAIN_NEWS : Url.TEST_NEWS) + data.get(position).getId());
                intent.putExtra("id", data.get(position).getId());
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
        ZixunApi.getInweHot(this, page, new JsonCallback<LzyResponse<ArticleListBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArticleListBean>> response) {
                if (!response.isFromCache()) {
                    if (!isFirst) {
                        isFirst = true;
                    }
                }
                LoadSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<ArticleListBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                if (page != 1) {
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

    private void LoadSuccess(Response<LzyResponse<ArticleListBean>> response) {
        int start = 0;
        int count = 0;
        if (page == 1) {
            data.clear();
            if (1 >= response.body().data.getLast_page()) {
                isEnd = true;
                swipeRefresh.setEnabled(false);
            }
        } else {
            if (page >= response.body().data.getLast_page()) {
                isEnd = true;
                swipeRefresh.setEnabled(false);
            }
        }

        if (null != response.body().data.getData()) {
            count = response.body().data.getData().size();
            Collections.reverse(response.body().data.getData());
            data.addAll(0, response.body().data.getData());
        }
        adapter.notifyItemRangeInserted(start, count);
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()== Constant.EVENT_NOTIFY){
            marks.setOpenTip((Boolean) event.getData());
        }
    }
}
