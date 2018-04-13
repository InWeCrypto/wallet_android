package com.inwecrypto.wallet.ui.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.Rank3Bean;
import com.inwecrypto.wallet.bean.Rank4Bean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.NoTradingActivity;
import com.inwecrypto.wallet.ui.news.RankingProjectActivity;
import com.inwecrypto.wallet.ui.news.TradingActivity;
import com.inwecrypto.wallet.ui.news.adapter.Ranking4Adapter;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/4/2 14:16
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class Ranking4Fragment extends BaseFragment {


    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.pingfen)
    LinearLayout pingfen;
    @BindView(R.id.erdu)
    LinearLayout erdu;

    private ArrayList<Rank4Bean> data = new ArrayList<>();
    private Ranking4Adapter adapter;

    private boolean erduSore=true;
    private boolean xiangmuSore;

    @Override
    protected int setLayoutID() {
        return R.layout.ranking4_fragment_layout;
    }

    @Override
    protected void initView() {
        setOpenEventBus(true);
        pingfen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (xiangmuSore){
                    img1.setImageResource(R.mipmap.paixu_icon_down);
                    Collections.sort(data, new Comparator<Rank4Bean>() {
                        @Override
                        public int compare(Rank4Bean a, Rank4Bean b) {
                            if (Float.parseFloat(null==a.getScore()?"0":a.getScore())>Float.parseFloat(null==b.getScore()?"0":b.getScore())){
                                return 1;
                            }else if (Float.parseFloat(null==a.getScore()?"0":a.getScore())<Float.parseFloat(null==b.getScore()?"0":b.getScore())){
                                return -1;
                            }else {
                                return 0;
                            }
                        }
                    });
                    adapter.notifyDataSetChanged();
                    xiangmuSore=false;
                }else {
                    img1.setImageResource(R.mipmap.paixu_icon_up);
                    Collections.sort(data, new Comparator<Rank4Bean>() {
                        @Override
                        public int compare(Rank4Bean a, Rank4Bean b) {
                            if (Float.parseFloat(null==a.getScore()?"0":a.getScore())>Float.parseFloat(null==b.getScore()?"0":b.getScore())){
                                return -1;
                            }else if (Float.parseFloat(null==a.getScore()?"0":a.getScore())<Float.parseFloat(null==b.getScore()?"0":b.getScore())){
                                return 1;
                            }else {
                                return 0;
                            }
                        }
                    });
                    adapter.notifyDataSetChanged();
                    xiangmuSore=true;
                }
                img2.setImageResource(R.mipmap.paixu_icon);
            }
        });

        erdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (erduSore){
                    img2.setImageResource(R.mipmap.paixu_icon_down);
                    Collections.sort(data, new Comparator<Rank4Bean>() {
                        @Override
                        public int compare(Rank4Bean a, Rank4Bean b) {
                            if (a.getRank()>b.getRank()){
                                return -1;
                            }else if (a.getRank()<b.getRank()){
                                return 1;
                            }else {
                                return 0;
                            }
                        }
                    });
                    adapter.notifyDataSetChanged();
                    erduSore=false;
                }else {
                    img2.setImageResource(R.mipmap.paixu_icon_up);
                    Collections.sort(data, new Comparator<Rank4Bean>() {
                        @Override
                        public int compare(Rank4Bean a, Rank4Bean b) {
                            if (a.getRank()>b.getRank()){
                                return 1;
                            }else if (a.getRank()<b.getRank()){
                                return -1;
                            }else {
                                return 0;
                            }
                        }
                    });
                    adapter.notifyDataSetChanged();
                    erduSore=true;
                }
                img1.setImageResource(R.mipmap.paixu_icon);
            }
        });

        adapter=new Ranking4Adapter(getActivity(),R.layout.ranking4_item_layout,data);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        ZixunApi.getCategoryRanking(this, new JsonCallback<LzyResponse<ArrayList<Rank4Bean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<Rank4Bean>>> response) {
                data.clear();
                if (null != response.body().data) {
                    data.addAll(response.body().data);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()== Constant.EVENT_PAIXU4){
            if (null==data||null==data.get(event.getKey1()).getCategory()){
                return;
            }
            if (1 == data.get(event.getKey1()).getCategory().getType()) {
                Intent intent = new Intent(mActivity, TradingActivity.class);
                intent.putExtra("project", data.get(event.getKey1()).getCategory());
                intent.putExtra("type", 0);
                keepTogo(intent);
            } else {
                Intent intent = new Intent(mActivity, NoTradingActivity.class);
                intent.putExtra("project", data.get(event.getKey1()).getCategory());
                intent.putExtra("type", 0);
                keepTogo(intent);
            }
        }
    }

}
