package com.inwecrypto.wallet.ui.info.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.NewsBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.InfoApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.MultiItemTypeSupport;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.info.adapter.InfoAdapter;
import com.inwecrypto.wallet.ui.me.activity.CommonWebActivity;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2017/12/1 14:47
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class AllInfoFragment extends BaseFragment {

    @BindView(R.id.mail_list)
    RecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private ArrayList<NewsBean> infoBeans = new ArrayList<>();
    private InfoAdapter adapter;
    private String type;

    @Override
    protected int setLayoutID() {
        return R.layout.info_fragment_info;
    }

    @Override
    protected void initView() {
        Bundle bundle=getArguments();
        if (null!=bundle){
            type=bundle.getString("type");
        }
        isOpenEventBus=true;
        setLazyOpen(true);

        MultiItemTypeSupport multiItemSupport = new MultiItemTypeSupport<NewsBean>() {
            @Override
            public int getLayoutId(int itemType) {
                //根据itemType返回item布局文件id
                switch (itemType) {
                    case 1:
                        return R.layout.info_item_info_2;
                    case 2:
                    case 3:
                        return R.layout.info_item_info_1;
                }
                return 0;
            }

            @Override
            public int getItemViewType(int postion, NewsBean infoBean) {
                return infoBean.getType();
            }
        };
        adapter = new InfoAdapter(getContext(), infoBeans, multiItemSupport);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);
        list.setOnScrollListener(new RecyclerView.OnScrollListener(){
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
                if (null == infoBeans || infoBeans.size() == 0) {
                    return;
                }
                if (null != infoBeans.get(position).getUrl()) {
                    Intent intent = new Intent(mActivity, CommonWebActivity.class);
                    intent.putExtra("title", infoBeans.get(position).getTitle());
                    intent.putExtra("url", infoBeans.get(position).getUrl().startsWith("http") ? infoBeans.get(position).getUrl() : Url.WEB_ROOT + infoBeans.get(position).getUrl().replace("../", ""));
                    keepTogo(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    @Override
    protected void loadData() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
        InfoApi.getInfo(this,type, new JsonCallback<LzyResponse<ArrayList<NewsBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<NewsBean>>> response) {
                swipeRefresh.setRefreshing(false);
                if (null != response) {
                    if (null != response.body().data) {
                        infoBeans.clear();
                        infoBeans.addAll(response.body().data);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(Response<LzyResponse<ArrayList<NewsBean>>> response) {
                super.onError(response);
                swipeRefresh.setRefreshing(false);
                ToastUtil.show(R.string.load_error);
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
