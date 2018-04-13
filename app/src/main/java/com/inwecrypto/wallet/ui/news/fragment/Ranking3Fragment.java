package com.inwecrypto.wallet.ui.news.fragment;

import android.content.Intent;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.Rank2Bean;
import com.inwecrypto.wallet.bean.Rank3Bean;
import com.inwecrypto.wallet.bean.Rank4Bean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.ProjectNewsWebActivity;
import com.inwecrypto.wallet.ui.news.adapter.Ranking2Adapter;
import com.inwecrypto.wallet.ui.news.adapter.Ranking3Adapter;
import com.kelin.scrollablepanel.library.ScrollablePanel;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/4/2 14:16
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class Ranking3Fragment extends BaseFragment {
    @BindView(R.id.scrollable_panel)
    ScrollablePanel scrollablePanel;

    private Ranking3Adapter adapter;

    private ArrayList<Rank3Bean> data=new ArrayList<>();

    @Override
    protected int setLayoutID() {
        return R.layout.ranking_fragment_layout;
    }

    @Override
    protected void initView() {
        setOpenEventBus(true);
        adapter=new Ranking3Adapter(getContext());
    }

    @Override
    protected void loadData() {
        ZixunApi.getDapp(this, new JsonCallback<LzyResponse<ArrayList<Rank3Bean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<Rank3Bean>>> response) {
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
        if (event.getEventCode()== Constant.EVENT_PAIXU3){

            if (event.getKey1()==-11){
                if (event.getKey2()==1){
                    Collections.sort(data, new Comparator<Rank3Bean>() {
                        @Override
                        public int compare(Rank3Bean a, Rank3Bean b) {
                            if (Float.parseFloat(null==a.getBalance()?"0":a.getBalance())>Float.parseFloat(null==b.getBalance()?"0":b.getBalance())){
                                return -1;
                            }else if (Float.parseFloat(null==a.getBalance()?"0":a.getBalance())<Float.parseFloat(null==b.getBalance()?"0":b.getBalance())){
                                return 1;
                            }else {
                                return 0;
                            }
                        }
                    });
                }else {
                    Collections.sort(data, new Comparator<Rank3Bean>() {
                        @Override
                        public int compare(Rank3Bean a, Rank3Bean b) {
                            if (Float.parseFloat(null==a.getBalance()?"0":a.getBalance())>Float.parseFloat(null==b.getBalance()?"0":b.getBalance())){
                                return 1;
                            }else if (Float.parseFloat(null==a.getBalance()?"0":a.getBalance())<Float.parseFloat(null==b.getBalance()?"0":b.getBalance())){
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
                    Collections.sort(data, new Comparator<Rank3Bean>() {
                        @Override
                        public int compare(Rank3Bean a, Rank3Bean b) {
                            if (Float.parseFloat(null==a.getDauLastDay()?"0":a.getDauLastDay())>Float.parseFloat(null==b.getDauLastDay()?"0":b.getDauLastDay())){
                                return -1;
                            }else if (Float.parseFloat(null==a.getDauLastDay()?"0":a.getDauLastDay())<Float.parseFloat(null==b.getDauLastDay()?"0":b.getDauLastDay())){
                                return 1;
                            }else {
                                return 0;
                            }
                        }
                    });
                }else {
                    Collections.sort(data, new Comparator<Rank3Bean>() {
                        @Override
                        public int compare(Rank3Bean a, Rank3Bean b) {
                            if (Float.parseFloat(null==a.getDauLastDay()?"0":a.getDauLastDay())>Float.parseFloat(null==b.getDauLastDay()?"0":b.getDauLastDay())){
                                return 1;
                            }else if (Float.parseFloat(null==a.getDauLastDay()?"0":a.getDauLastDay())<Float.parseFloat(null==b.getDauLastDay()?"0":b.getDauLastDay())){
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
                    Collections.sort(data, new Comparator<Rank3Bean>() {
                        @Override
                        public int compare(Rank3Bean a, Rank3Bean b) {
                            if (Float.parseFloat(null==a.getVolumeLastDay()?"0":a.getVolumeLastDay())>Float.parseFloat(null==b.getVolumeLastDay()?"0":b.getVolumeLastDay())){
                                return -1;
                            }else if (Float.parseFloat(null==a.getVolumeLastDay()?"0":a.getVolumeLastDay())<Float.parseFloat(null==b.getVolumeLastDay()?"0":b.getVolumeLastDay())){
                                return 1;
                            }else {
                                return 0;
                            }
                        }
                    });
                }else {
                    Collections.sort(data, new Comparator<Rank3Bean>() {
                        @Override
                        public int compare(Rank3Bean a, Rank3Bean b) {
                            if (Float.parseFloat(null==a.getVolumeLastDay()?"0":a.getVolumeLastDay())>Float.parseFloat(null==b.getVolumeLastDay()?"0":b.getVolumeLastDay())){
                                return 1;
                            }else if (Float.parseFloat(null==a.getVolumeLastDay()?"0":a.getVolumeLastDay())<Float.parseFloat(null==b.getVolumeLastDay()?"0":b.getVolumeLastDay())){
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
                    Collections.sort(data, new Comparator<Rank3Bean>() {
                        @Override
                        public int compare(Rank3Bean a, Rank3Bean b) {
                            if (Float.parseFloat(null==a.getTxLastDay()?"0":a.getTxLastDay())>Float.parseFloat(null==b.getTxLastDay()?"0":b.getTxLastDay())){
                                return -1;
                            }else if (Float.parseFloat(null==a.getTxLastDay()?"0":a.getTxLastDay())<Float.parseFloat(null==b.getTxLastDay()?"0":b.getTxLastDay())){
                                return 1;
                            }else {
                                return 0;
                            }
                        }
                    });
                }else {
                    Collections.sort(data, new Comparator<Rank3Bean>() {
                        @Override
                        public int compare(Rank3Bean a, Rank3Bean b) {
                            if (Float.parseFloat(null==a.getTxLastDay()?"0":a.getTxLastDay())>Float.parseFloat(null==b.getTxLastDay()?"0":b.getTxLastDay())){
                                return 1;
                            }else if (Float.parseFloat(null==a.getTxLastDay()?"0":a.getTxLastDay())<Float.parseFloat(null==b.getTxLastDay()?"0":b.getTxLastDay())){
                                return -1;
                            }else {
                                return 0;
                            }
                        }
                    });
                }
                scrollablePanel.notifyDataSetChanged();
            }else {
                Intent intent=new Intent(mActivity,ProjectNewsWebActivity.class);
                intent.putExtra("title",data.get(event.getKey1()).getTitle());
                intent.putExtra("url",data.get(event.getKey1()).getUrl());
                intent.putExtra("show",false);
                keepTogo(intent);
            }

        }
    }
}
