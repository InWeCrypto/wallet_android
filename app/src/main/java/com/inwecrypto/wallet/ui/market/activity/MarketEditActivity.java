package com.inwecrypto.wallet.ui.market.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.MarkeListBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MarketApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.market.adapter.MarketEditAdapter;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class MarketEditActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.market_list)
    SwipeMenuRecyclerView unitList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private ArrayList<MarkeListBean> marketBeens=new ArrayList<>();
    private MarketEditAdapter adapter;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.market_activity_edit_market;
    }

    @Override
    protected void initView() {
        txtMainTitle.setText(R.string.bianjihangqing);
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtRightTitle.setText(R.string.baocun_space);
        txtRightTitle.setCompoundDrawables(null,null,null,null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray jsonArray=new JSONArray();
                try {
                    int size=marketBeens.size();
                    for (int i=0;i<size;i++){
                        JSONObject market=new JSONObject();
                        market.putOpt("id",marketBeens.get(i).getId());
                        market.putOpt("sort",(size-i)+"");
                        jsonArray.put(market);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MarketApi.setMarket(mActivity,jsonArray.toString(), new JsonCallback<LzyResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<Object>> response) {
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFRESH));
                        finish();
                    }
                });
            }
        });


        adapter=new MarketEditAdapter(this,R.layout.market_item_edit_market,marketBeens);
        unitList.setLayoutManager(new LinearLayoutManager(this));
        unitList.setAdapter(adapter);
        unitList.setLongPressDragEnabled(true); // 开启拖拽。
        unitList.setOnItemMoveListener(mItemMoveListener);// 监听拖拽，更新UI。

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

    }

    @Override
    protected void initData() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
        MarketApi.market(mActivity, new JsonCallback<LzyResponse<ArrayList<MarkeListBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<MarkeListBean>>> response) {
                LoadSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<ArrayList<MarkeListBean>>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    OnItemMoveListener mItemMoveListener = new OnItemMoveListener() {
        @Override
        public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
            int fromPosition = srcHolder.getAdapterPosition();
            int toPosition = targetHolder.getAdapterPosition();

            // Item被拖拽时，交换数据，并更新adapter。
            Collections.swap(marketBeens, fromPosition, toPosition);
            adapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
            int position = srcHolder.getAdapterPosition();
            // Item被侧滑删除时，删除数据，并更新adapter。
            marketBeens.remove(position);
            adapter.notifyItemRemoved(position);
        }
    };

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    private void LoadSuccess(Response<LzyResponse<ArrayList<MarkeListBean>>> response) {
        marketBeens.clear();
        marketBeens.addAll(response.body().data);
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }
}
