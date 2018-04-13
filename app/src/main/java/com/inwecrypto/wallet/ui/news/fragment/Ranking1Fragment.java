package com.inwecrypto.wallet.ui.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.CommonDataBean;
import com.inwecrypto.wallet.bean.Rank1Bean;
import com.inwecrypto.wallet.bean.Rank3Bean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.RankingProjectActivity;
import com.inwecrypto.wallet.ui.news.adapter.Ranking1Adapter;
import com.kelin.scrollablepanel.library.ScrollablePanel;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
        setOpenEventBus(true);
        adapter=new Ranking1Adapter(getContext());
    }

    @Override
    protected void loadData() {
        ZixunApi.getMarketCap(this, new JsonCallback<LzyResponse<ArrayList<Rank1Bean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<Rank1Bean>>> response) {
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
        if (event.getEventCode()== Constant.EVENT_PAIXU1){
            if (event.getKey1()==-10){
                if (event.getKey2()==1){
                    Collections.sort(data, new Comparator<Rank1Bean>() {
                        @Override
                        public int compare(Rank1Bean a, Rank1Bean b) {
                            if (Float.parseFloat(null==a.getMarket()?"0":a.getMarket())>Float.parseFloat(null==b.getMarket()?"0":b.getMarket())){
                                return -1;
                            }else if (Float.parseFloat(null==a.getMarket()?"0":a.getMarket())<Float.parseFloat(null==b.getMarket()?"0":b.getMarket())){
                                return 1;
                            }else {
                                return 0;
                            }
                        }
                    });
                }else {
                    Collections.sort(data, new Comparator<Rank1Bean>() {
                        @Override
                        public int compare(Rank1Bean a, Rank1Bean b) {
                            if (Float.parseFloat(null==a.getMarket()?"0":a.getMarket())>Float.parseFloat(null==b.getMarket()?"0":b.getMarket())){
                                return 1;
                            }else if (Float.parseFloat(null==a.getMarket()?"0":a.getMarket())<Float.parseFloat(null==b.getMarket()?"0":b.getMarket())){
                                return -1;
                            }else {
                                return 0;
                            }
                        }
                    });
                }
                scrollablePanel.notifyDataSetChanged();
            }else if (event.getKey1()==-11){
                if (event.getKey2()==1){
                    Collections.sort(data, new Comparator<Rank1Bean>() {
                        @Override
                        public int compare(Rank1Bean a, Rank1Bean b) {
                            if (Float.parseFloat(null==a.getVolume()?"0":a.getVolume())>Float.parseFloat(null==b.getVolume()?"0":b.getVolume())){
                                return -1;
                            }else if (Float.parseFloat(null==a.getVolume()?"0":a.getVolume())<Float.parseFloat(null==b.getVolume()?"0":b.getVolume())){
                                return 1;
                            }else {
                                return 0;
                            }
                        }
                    });
                }else {
                    Collections.sort(data, new Comparator<Rank1Bean>() {
                        @Override
                        public int compare(Rank1Bean a, Rank1Bean b) {
                            if (Float.parseFloat(null==a.getVolume()?"0":a.getVolume())>Float.parseFloat(null==b.getVolume()?"0":b.getVolume())){
                                return 1;
                            }else if (Float.parseFloat(null==a.getVolume()?"0":a.getVolume())<Float.parseFloat(null==b.getVolume()?"0":b.getVolume())){
                                return -1;
                            }else {
                                return 0;
                            }
                        }
                    });
                }
                scrollablePanel.notifyDataSetChanged();

            }else if (event.getKey1()==-12){
                if (event.getKey2()==1){
                    Collections.sort(data, new Comparator<Rank1Bean>() {
                        @Override
                        public int compare(Rank1Bean a, Rank1Bean b) {
                            if (Float.parseFloat(null==a.getPrice()?"0":a.getPrice())>Float.parseFloat(null==b.getPrice()?"0":b.getPrice())){
                                return -1;
                            }else if (Float.parseFloat(null==a.getPrice()?"0":a.getPrice())<Float.parseFloat(null==b.getPrice()?"0":b.getPrice())){
                                return 1;
                            }else {
                                return 0;
                            }
                        }
                    });
                }else {
                    Collections.sort(data, new Comparator<Rank1Bean>() {
                        @Override
                        public int compare(Rank1Bean a, Rank1Bean b) {
                            if (Float.parseFloat(null==a.getPrice()?"0":a.getPrice())>Float.parseFloat(null==b.getPrice()?"0":b.getPrice())){
                                return 1;
                            }else if (Float.parseFloat(null==a.getPrice()?"0":a.getPrice())<Float.parseFloat(null==b.getPrice()?"0":b.getPrice())){
                                return -1;
                            }else {
                                return 0;
                            }
                        }
                    });
                }
                scrollablePanel.notifyDataSetChanged();

            }else if (event.getKey1()==-13){
                if (event.getKey2()==1){
                    Collections.sort(data, new Comparator<Rank1Bean>() {
                        @Override
                        public int compare(Rank1Bean a, Rank1Bean b) {
                            if (Float.parseFloat(null==a.getChange()?"0":a.getChange().replace("%",""))>Float.parseFloat(null==b.getChange()?"0":b.getChange().replace("%",""))){
                                return -1;
                            }else if (Float.parseFloat(null==a.getChange()?"0":a.getChange().replace("%",""))<Float.parseFloat(null==b.getChange()?"0":b.getChange().replace("%",""))){
                                return 1;
                            }else {
                                return 0;
                            }
                        }
                    });
                }else {
                    Collections.sort(data, new Comparator<Rank1Bean>() {
                        @Override
                        public int compare(Rank1Bean a, Rank1Bean b) {
                            if (Float.parseFloat(null==a.getChange()?"0":a.getChange().replace("%",""))>Float.parseFloat(null==b.getChange()?"0":b.getChange().replace("%",""))){
                                return 1;
                            }else if (Float.parseFloat(null==a.getChange()?"0":a.getChange().replace("%",""))<Float.parseFloat(null==b.getChange()?"0":b.getChange().replace("%",""))){
                                return -1;
                            }else {
                                return 0;
                            }
                        }
                    });
                }
                scrollablePanel.notifyDataSetChanged();

            }else if (event.getKey1()==-14){
                if (event.getKey2()==1){
                    Collections.sort(data, new Comparator<Rank1Bean>() {
                        @Override
                        public int compare(Rank1Bean a, Rank1Bean b) {
                            if (Float.parseFloat(null==a.getMarket()?"0":a.getMarket())>Float.parseFloat(null==b.getMarket()?"0":b.getMarket())){
                                return -1;
                            }else if (Float.parseFloat(null==a.getMarket()?"0":a.getMarket())<Float.parseFloat(null==b.getMarket()?"0":b.getMarket())){
                                return 1;
                            }else {
                                return 0;
                            }
                        }
                    });
                }else {
                    Collections.sort(data, new Comparator<Rank1Bean>() {
                        @Override
                        public int compare(Rank1Bean a, Rank1Bean b) {
                            if (Float.parseFloat(null==a.getMarket()?"0":a.getMarket())>Float.parseFloat(null==b.getMarket()?"0":b.getMarket())){
                                return 1;
                            }else if (Float.parseFloat(null==a.getMarket()?"0":a.getMarket())<Float.parseFloat(null==b.getMarket()?"0":b.getMarket())){
                                return -1;
                            }else {
                                return 0;
                            }
                        }
                    });
                }
                scrollablePanel.notifyDataSetChanged();
            }else {
                Intent intent=new Intent(mActivity, RankingProjectActivity.class);
                intent.putExtra("project",data.get(event.getKey1()));
                keepTogo(intent);
            }
        }
    }
}
