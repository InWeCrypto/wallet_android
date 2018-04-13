package com.inwecrypto.wallet.ui.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.Rank4Bean;
import com.inwecrypto.wallet.bean.Rank5Bean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.ProjectNewsWebActivity;
import com.inwecrypto.wallet.ui.news.adapter.Ranking5Adapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：xiaoji06 on 2018/4/2 14:16
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class Ranking5Fragment extends BaseFragment {


    @BindView(R.id.list)
    RecyclerView list;

    private ArrayList<Rank5Bean.DataBean> data = new ArrayList<>();
    private Ranking5Adapter adapter;

    @Override
    protected int setLayoutID() {
        return R.layout.ranking5_fragment_layout;
    }

    @Override
    protected void initView() {

        adapter = new Ranking5Adapter(getActivity(), R.layout.ranking5_item_layout, data);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mActivity, ProjectNewsWebActivity.class);
                intent.putExtra("title", data.get(position).getTitle());
                intent.putExtra("url", (App.isMain ? Url.MAIN_NEWS : Url.TEST_NEWS) + data.get(position).getId());
                intent.putExtra("id", data.get(position).getId());
                intent.putExtra("decs",data.get(position).getDesc());
                intent.putExtra("img",data.get(position).getImg());
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
        ZixunApi.getHotArticle(this, new JsonCallback<LzyResponse<Rank5Bean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<Rank5Bean>> response) {
                data.clear();
                if (null != response.body().data) {
                    data.addAll(response.body().data.getData());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
