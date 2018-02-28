package com.inwecrypto.wallet.ui.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.ArticleDetaileBean;
import com.inwecrypto.wallet.bean.ArticleListBean;
import com.inwecrypto.wallet.bean.ProjectDetaileBean;
import com.inwecrypto.wallet.bean.ProjectListBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.UserApi;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.EndLessOnScrollListener;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.adapter.ShoucangAdapter;
import com.inwecrypto.wallet.ui.news.ProjectNewsWebActivity;
import com.lzy.okgo.model.Response;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/2/7 14:48
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ShoucangActivity extends BaseActivity {
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.list)
    SwipeMenuRecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;

    private ArrayList<ArticleDetaileBean> data=new ArrayList<>();
    private ShoucangAdapter adapter;

    private int page=1;
    private boolean isEnd;
    private boolean isShow;
    private LinearLayoutManager layoutManager;
    private EndLessOnScrollListener scrollListener;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.shoucang_activity;
    }

    @Override
    protected void initView() {

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(R.string.wodeshoucang);

        txtRightTitle.setVisibility(View.GONE);

        adapter = new ShoucangAdapter(this, R.layout.shoucang_item, data);
        layoutManager=new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        list.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mActivity);
                deleteItem.setText(getString(R.string.quxiaoshoucang));
                deleteItem.setTextSize(14);
                deleteItem.setTextColorResource(R.color.c_ffffff);
                deleteItem.setBackgroundColorResource(R.color.c_FF841C);
                deleteItem.setWidth(DensityUtil.dip2px(mActivity, 110));
                deleteItem.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                // 各种文字和图标属性设置。
                swipeRightMenu.addMenuItem(deleteItem); // 在Item左侧添加一个菜单。
            }
        });

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                isEnd=false;
                isShow=false;
                scrollListener.reset();
                //从网络获取钱包
                initData();
            }
        });

        scrollListener=new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                if (isEnd){
                    if (!isShow&&page!=1){
                        ToastUtil.show(getString(R.string.zanwugengduoshuju));
                        isShow=true;
                    }
                }else {
                    page++;
                    initData();
                }
            }
        };
        list.addOnScrollListener(scrollListener);

        list.setAdapter(adapter);
        list.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                if (menuBridge.getPosition() == 0) {//删除
                    showFixLoading();
                    ZixunApi.newsCollect(mActivity, data.get(menuBridge.getAdapterPosition()).getId(), false, new JsonCallback<LzyResponse<Object>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<Object>> response) {
                            list.smoothCloseMenu();
                            ToastUtil.show(getString(R.string.quxiaochenggong));
                            initData();
                        }

                        @Override
                        public void onError(Response<LzyResponse<Object>> response) {
                            super.onError(response);
                            ToastUtil.show(getString(R.string.quxiaoshibai));
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            hideFixLoading();
                        }
                    });
                }
            }
        });

        list.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent intent=new Intent(mActivity,ProjectNewsWebActivity.class);
                intent.putExtra("title",data.get(position).getTitle());
                intent.putExtra("url", (App.isMain? Url.MAIN_NEWS:Url.TEST_NEWS)+data.get(position).getId());
                intent.putExtra("id",data.get(position).getId());
                keepTogo(intent);
            }
        });
    }

    @Override
    protected void initData() {
        UserApi.getFavorite(this
                , page
                , new JsonCallback<LzyResponse<ArticleListBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<ArticleListBean>> response) {
                        if (page==1){
                            data.clear();
                        }

                        if (null != response.body().data.getData()) {
                            data.addAll(response.body().data.getData());
                        }

                        if (page>=response.body().data.getCurrent_page()){
                            //最后一页
                            isEnd=true;
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Response<LzyResponse<ArticleListBean>> response) {
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

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
