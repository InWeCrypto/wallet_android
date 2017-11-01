package com.inwecrypto.wallet.ui.market.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.MarketAddBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MarketApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.market.adapter.MarketAddAdapter;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class MarketAddActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.unit_list)
    RecyclerView unitList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.search)
    EditText search;

    private ArrayList<MarketAddBean.DataBean> marketBeens=new ArrayList<>();
    private MarketAddAdapter adapter;
    private int position=-1;
    private boolean move;
    private int mIndex = 0;
    private LinearLayoutManager unitManager;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_add_wallet;
    }

    @Override
    protected void initView() {
        txtMainTitle.setText(R.string.tianjiahangqing);
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtRightTitle.setText(R.string.save);
        txtRightTitle.setCompoundDrawables(null,null,null,null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb=new StringBuilder();
                sb.append("[");
                for (int i=0;i<marketBeens.size();i++){
                    if (marketBeens.get(i).getRelation_user_count()==1){
                        sb.append(marketBeens.get(i).getId()+",");
                    }
                }
                if(sb.length()!=1){
                    sb.delete(sb.length() - 1, sb.length());
                }
                sb.append("]");
                MarketApi.market(mActivity,sb.toString(), new JsonCallback<LzyResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<Object>> response) {
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFRESH));
                        ToastUtil.show("添加成功");
                        finish();
                    }
                });
            }
        });


        adapter=new MarketAddAdapter(this,R.layout.market_item_add_market,marketBeens);
        unitManager=new LinearLayoutManager(this);
        unitList.setLayoutManager(unitManager);
        unitList.setAdapter(adapter);
        unitList.setNestedScrollingEnabled(false);
        unitList.addOnScrollListener(new RecyclerViewListener());
        unitList.setOnScrollListener(new RecyclerView.OnScrollListener(){
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

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (marketBeens.get(position).getType()==2){
                    return;
                }
                if (0==marketBeens.get(position).getRelation_user_count()){
                    marketBeens.get(position).setRelation_user_count(1);
                }else {
                    marketBeens.get(position).setRelation_user_count(0);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    boolean has=false;
                    for (int i=0;i<marketBeens.size();i++){
                        if (marketBeens.get(i).getName().toUpperCase().equals(search.getText().toString().toUpperCase())){
                            has=true;
                            position=i;
                            break;
                        }
                    }
                    if (has){
                        move(position);
                    }else {
                        ToastUtil.show("未找到该行情");
                    }
                    return true;
                }
                return false;

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
        MarketApi.marketAdd(mActivity,1, new JsonCallback<LzyResponse<CommonListBean<MarketAddBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<MarketAddBean>>> response) {
                LoadSuccess(response);
                swipeRefresh.setEnabled(false);
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<MarketAddBean>>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    private void LoadSuccess(Response<LzyResponse<CommonListBean<MarketAddBean>>> response) {
        marketBeens.clear();
        ArrayList<MarketAddBean> list = response.body().data.getList();
        if (null!=list&&list.size()>0){
            for (int i=0;i<list.size();i++){
                MarketAddBean.DataBean title=new MarketAddBean.DataBean();
                title.setName(list.get(i).getName());
                title.setType(2);
                marketBeens.add(title);
                marketBeens.addAll(list.get(i).getData());
            }
        }
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }

    private void move(int position){
        mIndex = position;
        unitList.stopScroll();
        smoothMoveToPosition(position);
    }

    private void smoothMoveToPosition(int n) {

        int firstItem = unitManager.findFirstVisibleItemPosition();
        int lastItem = unitManager.findLastVisibleItemPosition();
        if (n <= firstItem ){
            unitList.smoothScrollToPosition(n);
        }else if ( n <= lastItem ){
            int top = unitList.getChildAt(n - firstItem).getTop();
            unitList.smoothScrollBy(0, top);
        }else{
            unitList.smoothScrollToPosition(n);
            move = true;
        }

    }

    class RecyclerViewListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE){
                move = false;
                int n = mIndex - unitManager.findFirstVisibleItemPosition();
                if ( 0 <= n && n < unitList.getChildCount()){
                    int top = unitList.getChildAt(n).getTop();
                    unitList.smoothScrollBy(0, top);
                }

            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    }
}
