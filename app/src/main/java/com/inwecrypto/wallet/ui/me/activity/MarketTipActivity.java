package com.inwecrypto.wallet.ui.me.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.ArticleDetaileBean;
import com.inwecrypto.wallet.bean.ArticleListBean;
import com.inwecrypto.wallet.bean.ProjectDetaileBean;
import com.inwecrypto.wallet.bean.ProjectListBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.UserApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.EndLessOnScrollListener;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.adapter.MarketTipAdapter;
import com.inwecrypto.wallet.ui.me.adapter.ShoucangAdapter;
import com.inwecrypto.wallet.ui.newneo.ImportWalletFragment;
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

public class MarketTipActivity extends BaseActivity {

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

    private ArrayList<ProjectDetaileBean> data=new ArrayList<>();
    private MarketTipAdapter adapter;

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
        return R.layout.market_tip_activity;
    }

    @Override
    protected void initView() {

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(R.string.wodehangqingtixing);

        txtRightTitle.setVisibility(View.GONE);

        adapter = new MarketTipAdapter(this, R.layout.market_tip_item, data);
        layoutManager=new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        list.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mActivity);
                deleteItem.setText(getString(R.string.bianji));
                deleteItem.setTextSize(14);
                deleteItem.setTextColorResource(R.color.c_ffffff);
                deleteItem.setBackgroundColorResource(R.color.c_008c55);
                deleteItem.setWidth(DensityUtil.dip2px(mActivity, 72));
                deleteItem.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                // 各种文字和图标属性设置。
                swipeRightMenu.addMenuItem(deleteItem); // 在Item左侧添加一个菜单。

                SwipeMenuItem upItem = new SwipeMenuItem(mActivity);
                upItem.setText(getString(R.string.shanchu));
                upItem.setTextSize(14);
                upItem.setTextColorResource(R.color.c_ffffff);
                upItem.setBackgroundColorResource(R.color.c_FF841C);
                upItem.setWidth(DensityUtil.dip2px(mActivity, 72));
                upItem.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                // 各种文字和图标属性设置。
                swipeRightMenu.addMenuItem(upItem); // 在Item右侧添加一个菜单。
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
            public void onItemClick(final SwipeMenuBridge menuBridge) {
                if (menuBridge.getPosition() == 0) {//编辑
                    FragmentManager fm = getSupportFragmentManager();
                    AddMarketTipFragment improt = new AddMarketTipFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("price", data.get(menuBridge.getAdapterPosition()).getIco().getPrice_usd());
                    bundle.putString("high",null==data.get(menuBridge.getAdapterPosition()).getCategory_user().getMarket_hige()?"0":data.get(menuBridge.getAdapterPosition()).getCategory_user().getMarket_hige());
                    bundle.putString("low",null==data.get(menuBridge.getAdapterPosition()).getCategory_user().getMarket_lost()?"0":data.get(menuBridge.getAdapterPosition()).getCategory_user().getMarket_lost());
                    improt.setArguments(bundle);
                    improt.show(fm, "tip");
                    improt.setOnNextListener(new AddMarketTipFragment.OnNextInterface() {
                        @Override
                        public void onNext(String hight, String low, final Dialog dialog) {
                            showFixLoading();
                            UserApi.follow(mActivity
                                    , data.get(menuBridge.getAdapterPosition()).getId()
                                    , true
                                    , hight
                                    , low
                                    , new JsonCallback<LzyResponse<Object>>() {
                                        @Override
                                        public void onSuccess(Response<LzyResponse<Object>> response) {
                                            list.smoothCloseMenu();
                                            dialog.dismiss();
                                            ToastUtil.show(getString(R.string.bianjichengong));
                                            initData();
                                        }

                                        @Override
                                        public void onError(Response<LzyResponse<Object>> response) {
                                            super.onError(response);
                                            ToastUtil.show(getString(R.string.bianjishibai));
                                        }

                                        @Override
                                        public void onFinish() {
                                            super.onFinish();
                                            hideFixLoading();
                                        }
                                    });
                        }
                    });

                }else if (menuBridge.getPosition() == 1) {//删除
                    showFixLoading();
                    UserApi.follow(mActivity
                            , data.get(menuBridge.getAdapterPosition()).getId()
                            , false
                            , ""
                            , ""
                            , new JsonCallback<LzyResponse<Object>>() {
                                @Override
                                public void onSuccess(Response<LzyResponse<Object>> response) {
                                    list.smoothCloseMenu();
                                    ToastUtil.show(getString(R.string.shanchuchenggong));
                                    initData();
                                }

                                @Override
                                public void onError(Response<LzyResponse<Object>> response) {
                                    super.onError(response);
                                    ToastUtil.show(getString(R.string.shanchushibai));
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

            }
        });
    }

    @Override
    protected void initData() {
        UserApi.getMarketTip(this
                , page
                , new JsonCallback<LzyResponse<ProjectListBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<ProjectListBean>> response) {
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
                    public void onError(Response<LzyResponse<ProjectListBean>> response) {
                        super.onError(response);
                        if (page!=1){
                            page--;
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if(null!=swipeRefresh){
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
