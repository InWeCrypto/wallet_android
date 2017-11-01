package com.inwecrypto.wallet.ui.market.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.MarketRemindBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MarketApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.market.adapter.MarketRemindAdapter;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class MarketRemindActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.market_list)
    SwipeMenuRecyclerView marketList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private ArrayList<MarketRemindBean> marketBeens=new ArrayList<>();
    private MarketRemindAdapter adapter;


    @Override
    protected void getBundleExtras(Bundle extras) {
        isOpenEventBus=true;
    }

    @Override
    protected int setLayoutID() {
        return R.layout.market_activity_remind;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.tixing);
        Drawable drawableInfo= getResources().getDrawable(R.mipmap.btn_add);
        /// 这一步必须要做,否则不会显示.
        drawableInfo.setBounds(0, 0, drawableInfo.getMinimumWidth(), drawableInfo.getMinimumHeight());
        txtRightTitle.setCompoundDrawables(drawableInfo,null,null,null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepTogo(MarketRemindAddActivity.class);
            }
        });

        adapter=new MarketRemindAdapter(this,R.layout.market_item_remind_market,marketBeens);
        marketList.setLayoutManager(new LinearLayoutManager(this));
        marketList.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mActivity);
                deleteItem.setText(getString(R.string.delete));
                deleteItem.setTextSize(14);
                deleteItem.setTextColorResource(R.color.c_ffffff);
                deleteItem.setBackgroundColorResource(R.color.c_E86438);
                deleteItem.setWidth(DensityUtil.dip2px(mActivity,72));
                deleteItem.setHeight(MATCH_PARENT);
                // 各种文字和图标属性设置。
                swipeRightMenu.addMenuItem(deleteItem); // 在Item左侧添加一个菜单。

                SwipeMenuItem upItem = new SwipeMenuItem(mActivity);
                upItem.setText("编辑");
                upItem.setTextSize(14);
                upItem.setTextColorResource(R.color.c_ffffff);
                upItem.setBackgroundColorResource(R.color.c_bababb);
                upItem.setWidth(DensityUtil.dip2px(mActivity,72));
                upItem.setHeight(MATCH_PARENT);
                // 各种文字和图标属性设置。
                swipeRightMenu.addMenuItem(upItem); // 在Item右侧添加一个菜单。
            }
        });
        marketList.setAdapter(adapter);
        marketList.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                if (menuBridge.getPosition()==0){//删除
                    MarketApi.marketNotification(mActivity, marketBeens.get(menuBridge.getAdapterPosition()).getRelation_notification().get(0).getId(), new JsonCallback<LzyResponse<Object>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<Object>> response) {
                            marketList.smoothCloseMenu();
                            ToastUtil.show(getString(R.string.shanchuchenggong));
                            initData();
                        }

                        @Override
                        public void onError(Response<LzyResponse<Object>> response) {
                            super.onError(response);
                            ToastUtil.show(getString(R.string.shanchushibai));
                        }
                    });
                }else if (menuBridge.getPosition()==1){//编辑
                    marketList.smoothCloseMenu();
                    Intent intent=new Intent(mActivity,MarketRemindEditActivity.class);
                    intent.putExtra("market",marketBeens.get(menuBridge.getAdapterPosition()));
                    keepTogo(intent);
                }
            }
        });
        marketList.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefresh.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
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
        MarketApi.marketNotification(mActivity, new JsonCallback<LzyResponse<CommonListBean<MarketRemindBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<MarketRemindBean>>> response) {
                LoadSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<MarketRemindBean>>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void LoadSuccess(Response<LzyResponse<CommonListBean<MarketRemindBean>>> response) {
        ArrayList<MarketRemindBean> marketReminds=new ArrayList<>();
        if (null!=response.body().data.getList()){
            for (int i=0;i<response.body().data.getList().size();i++){
                if (response.body().data.getList().get(i).getRelation_notification_count()==1){
                    marketReminds.add(response.body().data.getList().get(i));
                }
            }
        }
        marketBeens.clear();
        marketBeens.addAll(marketReminds);
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()== Constant.EVENT_REFRESH){
            initData();
        }
    }

}
