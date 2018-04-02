package com.inwecrypto.wallet.ui.newneo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.GntBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.wallet.adapter.AllGntAdapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class NewNeoAddNep5Activity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.token_list)
    RecyclerView tokenList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.search)
    EditText search;

    private AllGntAdapter adapter;
    private ArrayList<GntBean> data = new ArrayList<>();
    private ArrayList<GntBean> beforeData = new ArrayList<>();

    private int id;
    private int walletId;
    private int position=-1;
    private boolean move;
    private int mIndex = 0;
    private LinearLayoutManager unitManager;

    @Override
    protected void getBundleExtras(Bundle extras) {
        id = extras.getInt("id");
        walletId = extras.getInt("walletId");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.newneo_wallet_activity_add_nep5;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.tianjianep5zichan);
        txtRightTitle.setText(R.string.wancheng);
        txtRightTitle.setCompoundDrawables(null, null, null, null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isSelect()) {
                        sb.append(data.get(i).getId() + ",");
                    }
                }
                if (sb.length() != 1) {
                    sb.delete(sb.length() - 1, sb.length());
                }
                sb.append("]");
                WalletApi.userGnt(mActivity, walletId, sb.toString(), new JsonCallback<LzyResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<Object>> response) {
                        EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_REFRESH));
                        finish();
                    }

                    @Override
                    public void onError(Response<LzyResponse<Object>> response) {
                        super.onError(response);
                        ToastUtil.show(getString(R.string.tianjiashibai));
                    }
                });
            }
        });

        adapter = new AllGntAdapter(this, R.layout.wallet_item_gnt, data);
        unitManager=new LinearLayoutManager(this);
        tokenList.setLayoutManager(unitManager);
        tokenList.setAdapter(adapter);
        tokenList.setNestedScrollingEnabled(false);
        tokenList.addOnScrollListener(new RecyclerViewListener());
        tokenList.setOnScrollListener(new RecyclerView.OnScrollListener(){
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
        tokenList.setOnScrollListener(new RecyclerView.OnScrollListener() {
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

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (data.get(position).isSelect()) {
                    data.get(position).setSelect(false);
                } else {
                    data.get(position).setSelect(true);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>0){
                    ArrayList<GntBean> nowData = new ArrayList<>();
                    for (GntBean gnt:beforeData){
                        if (gnt.getName().toUpperCase().contains(s.toString().toUpperCase())){
                            nowData.add(gnt);
                        }
                    }
                    data.clear();
                    data.addAll(nowData);
                    adapter.notifyDataSetChanged();
                }else {
                    data.clear();
                    data.addAll(beforeData);
                    adapter.notifyDataSetChanged();
                }
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
        WalletApi.gntCategory(mActivity, walletId, id, new JsonCallback<LzyResponse<CommonListBean<GntBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<GntBean>>> response) {
                data.clear();
                beforeData.clear();
                if (null != response.body().data && null != response.body().data.getList()) {
                    data.addAll(response.body().data.getList());
                    beforeData.addAll(response.body().data.getList());
                }
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<GntBean>>> response) {
                super.onError(response);
                if (NetworkUtils.isConnected(mActivity)){
                    ToastUtil.show(getString(R.string.load_error));
                }
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    private void move(int position){
        mIndex = position;
        tokenList.stopScroll();
        smoothMoveToPosition(position);
    }

    private void smoothMoveToPosition(int n) {

        int firstItem = unitManager.findFirstVisibleItemPosition();
        int lastItem = unitManager.findLastVisibleItemPosition();
        if (n <= firstItem ){
            tokenList.smoothScrollToPosition(n);
        }else if ( n <= lastItem ){
            int top = tokenList.getChildAt(n - firstItem).getTop();
            tokenList.smoothScrollBy(0, top);
        }else{
            tokenList.smoothScrollToPosition(n);
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
                if ( 0 <= n && n < tokenList.getChildCount()){
                    int top = tokenList.getChildAt(n).getTop();
                    tokenList.smoothScrollBy(0, top);
                }

            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    }
}
