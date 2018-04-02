package com.inwecrypto.wallet.ui.news.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.CommonDataBean;
import com.inwecrypto.wallet.bean.Rank1Bean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.adapter.Ranking1Adapter;
import com.kelin.scrollablepanel.library.ScrollablePanel;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：xiaoji06 on 2018/4/2 14:16
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class Ranking1Fragment extends BaseFragment {
    @BindView(R.id.scrollable_panel)
    ScrollablePanel scrollablePanel;

    private Ranking1Adapter adapter;

    private ArrayList<Rank1Bean> data=new ArrayList<>();

    @Override
    protected int setLayoutID() {
        return R.layout.ranking_fragment_layout;
    }

    @Override
    protected void initView() {
        adapter=new Ranking1Adapter(getContext());
    }

    @Override
    protected void loadData() {
        ZixunApi.getMarketCap(this, new JsonCallback<LzyResponse<ArrayList<Rank1Bean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<Rank1Bean>>> response) {
                data.clear();
                if (null!=response.body().data){
                    data.addAll(response.body().data);
                }
                adapter.setData(data);
                scrollablePanel.setPanelAdapter(adapter);
                scrollablePanel.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
