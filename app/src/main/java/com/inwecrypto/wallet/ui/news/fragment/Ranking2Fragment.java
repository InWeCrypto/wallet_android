package com.inwecrypto.wallet.ui.news.fragment;

import android.content.Intent;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.Rank1Bean;
import com.inwecrypto.wallet.bean.Rank2Bean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.ProjectNewsWebActivity;
import com.inwecrypto.wallet.ui.news.adapter.Ranking1Adapter;
import com.inwecrypto.wallet.ui.news.adapter.Ranking2Adapter;
import com.kelin.scrollablepanel.library.ScrollablePanel;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/4/2 14:16
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class Ranking2Fragment extends BaseFragment {
    @BindView(R.id.scrollable_panel)
    ScrollablePanel scrollablePanel;

    private Ranking2Adapter adapter;

    private ArrayList<Rank2Bean> data=new ArrayList<>();

    @Override
    protected int setLayoutID() {
        return R.layout.ranking_fragment_layout;
    }

    @Override
    protected void initView() {
        setOpenEventBus(true);
        adapter=new Ranking2Adapter(getContext());
    }

    @Override
    protected void loadData() {
        ZixunApi.getExchanges(this, new JsonCallback<LzyResponse<ArrayList<Rank2Bean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<Rank2Bean>>> response) {
                if (null==scrollablePanel){
                    return;
                }
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
        if (event.getEventCode()== Constant.EVENT_PAIXU2){
            Intent intent=new Intent(mActivity,ProjectNewsWebActivity.class);
            intent.putExtra("title",data.get(event.getKey1()).getName());
            intent.putExtra("url",data.get(event.getKey1()).getWebsite());
            intent.putExtra("show",false);
            keepTogo(intent);
        }
    }
}
