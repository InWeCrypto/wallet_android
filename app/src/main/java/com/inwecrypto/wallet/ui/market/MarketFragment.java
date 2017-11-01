package com.inwecrypto.wallet.ui.market;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.db.CacheManager;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import com.inwecrypto.wallet.AppApplication;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.MarkeListBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MarketApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.market.activity.MarketAddActivity;
import com.inwecrypto.wallet.ui.market.activity.MarketDetaileActivity;
import com.inwecrypto.wallet.ui.market.activity.MarketEditActivity;
import com.inwecrypto.wallet.ui.market.activity.MarketRemindActivity;
import com.inwecrypto.wallet.ui.market.adapter.MarketAdapter;

/**
 * Created by Administrator on 2017/7/15.
 * 功能描述：
 * 版本：@version
 */

public class MarketFragment extends BaseFragment {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.market_list)
    RecyclerView marketList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private ArrayList<MarkeListBean.DataBean> markets=new ArrayList<>();
    private MarketAdapter adapter;

    @Override
    protected int setLayoutID() {
        return R.layout.market_fragment;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setVisibility(View.GONE);
        txtMainTitle.setText(getString(R.string.hangqing));
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog();
            }
        });
        Drawable drawable= getResources().getDrawable(R.mipmap.btn_add);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        txtRightTitle.setCompoundDrawables(drawable,null,null,null);
        isOpenEventBus=true;

        adapter=new MarketAdapter(mContext,R.layout.market_item_market,markets);
        marketList.setLayoutManager(new LinearLayoutManager(mContext));
        marketList.setAdapter(adapter);
        marketList.getItemAnimator().setChangeDuration(0);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
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
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,MarketDetaileActivity.class);
                intent.putExtra("market",markets.get(position));
                keepTogo(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void loadData() {
        if (AppApplication.get().getSp().getBoolean(Constant.IS_CLOD,false)){
            return;
        }
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
        MarketApi.market(mFragment, new JsonCallback<LzyResponse<CommonListBean<MarkeListBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<MarkeListBean>>> response) {
                LoadSuccess(response);
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<CommonListBean<MarkeListBean>>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<MarkeListBean>>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (null!=swipeRefresh){
                    swipeRefresh.setRefreshing(false);
                }
            }
        });

    }

    private void LoadSuccess(Response<LzyResponse<CommonListBean<MarkeListBean>>> response) {
        markets.clear();
        ArrayList<MarkeListBean> list = response.body().data.getList();
        if (null!=list&&list.size()>0){
            for (int i=0;i<list.size();i++){
                markets.addAll(list.get(i).getData());
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void showSelectDialog() {
        // 产生背景变暗效果
        WindowManager.LayoutParams lp = getActivity().getWindow()
                .getAttributes();
        lp.alpha = 0.4f;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);

        View selectPopupWin = LayoutInflater.from(getContext()).inflate(R.layout.view_popup_market, null, false);
        TextView add=(TextView)selectPopupWin.findViewById(R.id.add);
        TextView edit = (TextView) selectPopupWin.findViewById(R.id.edit);
        TextView tixing = (TextView) selectPopupWin.findViewById(R.id.tixing);

        final PopupWindow window = new PopupWindow(selectPopupWin, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
        window.showAtLocation(txtRightTitle,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getActivity().getWindow().setAttributes(lp);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepTogo(MarketAddActivity.class);
                window.dismiss();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepTogo(MarketEditActivity.class);
                window.dismiss();
            }
        });

        tixing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepTogo(MarketRemindActivity.class);
                window.dismiss();
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()==Constant.EVENT_MARKET){
            refershData();
        }
    }

    @Override
    protected void initShow() {
        super.initShow();
        refershData();
        //获取数据
        MarketApi.market(mFragment, new JsonCallback<LzyResponse<CommonListBean<MarkeListBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<MarkeListBean>>> response) {
                LoadSuccess(response);
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<CommonListBean<MarkeListBean>>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<MarkeListBean>>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void refershData() {
        //获取缓存数据
        CacheEntity<LzyResponse<CommonListBean<MarkeListBean>>> cacheBean = (CacheEntity<LzyResponse<CommonListBean<MarkeListBean>>>) CacheManager.getInstance().get(Constant.MARKET);
        if (null!=cacheBean&&null!=cacheBean.getData()&&null!=cacheBean.getData().data.getList()&&cacheBean.getData().data.getList().size()>0){
            //刷新
            markets.clear();
            ArrayList<MarkeListBean> list = cacheBean.getData().data.getList();
            if (null!=list&&list.size()>0){
                for (int i=0;i<list.size();i++){
                    markets.addAll(list.get(i).getData());
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}
