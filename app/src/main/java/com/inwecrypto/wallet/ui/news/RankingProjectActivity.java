package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.MarketCapBean;
import com.inwecrypto.wallet.bean.ProjectMarketBean;
import com.inwecrypto.wallet.bean.Rank1Bean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.activity.CommonWebActivity;
import com.inwecrypto.wallet.ui.news.adapter.TradingMarketAdapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/4/2 17:31
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class RankingProjectActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.long_name)
    TextView longName;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.charge)
    TextView charge;
    @BindView(R.id.hight)
    TextView hight;
    @BindView(R.id.low)
    TextView low;
    @BindView(R.id.liang)
    TextView liang;
    @BindView(R.id.paiming)
    TextView paiming;
    @BindView(R.id.shizhi)
    TextView shizhi;
    @BindView(R.id.liutongliang)
    TextView liutongliang;
    @BindView(R.id.zongliang)
    TextView zongliang;
    @BindView(R.id.market_list)
    RecyclerView marketList;

    private Rank1Bean project;
    private MarketCapBean marketCapBean;

    private TradingMarketAdapter adapter;

    private ArrayList<ProjectMarketBean> data=new ArrayList<>();

    @Override
    protected void getBundleExtras(Bundle extras) {
        project= (Rank1Bean) extras.getSerializable("project");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.ranking_project_layout;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(R.string.xiangqing);
        txtRightTitle.setText(R.string.xiangmudongtai);
        txtRightTitle.setCompoundDrawables(null, null, null, null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null==marketCapBean||null==marketCapBean.getCategory()){
                    return;
                }
                if (1 == marketCapBean.getCategory().getType()) {
                    Intent intent = new Intent(mActivity, TradingActivity.class);
                    intent.putExtra("project", marketCapBean.getCategory());
                    intent.putExtra("type", 0);
                    keepTogo(intent);
                } else {
                    Intent intent = new Intent(mActivity, NoTradingActivity.class);
                    intent.putExtra("project", marketCapBean.getCategory());
                    intent.putExtra("type", 0);
                    keepTogo(intent);
                }
            }
        });

        adapter = new TradingMarketAdapter(this, R.layout.trading_market_item, data);
        marketList.setLayoutManager(new LinearLayoutManager(this));
        marketList.setAdapter(adapter);
        marketList.setNestedScrollingEnabled(false);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,CommonWebActivity.class);
                intent.putExtra("title", data.get(position).getPair());
                intent.putExtra("url", data.get(position).getUrl());
                keepTogo(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    @Override
    protected void initData() {
        ZixunApi.getMarketCap(this, project.getKey(), new JsonCallback<LzyResponse<MarketCapBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<MarketCapBean>> response) {
                marketCapBean=response.body().data;
                Glide.with(mActivity)
                        .load(marketCapBean.getImg())
                        .crossFade()
                        .into(img);
                name.setText(marketCapBean.getName());
                longName.setText("("+marketCapBean.getSymbol()+")");
                if (App.get().getUnit()==1){
                    price.setText("¥"+marketCapBean.getPrice_cny());
                    hight.setText("¥"+marketCapBean.getHigh_price_cny());
                    low.setText("¥"+marketCapBean.getLow_price_cny());
                    String volume="0.00";
                    if (new BigDecimal(marketCapBean.getVolume_cny()).floatValue() < 10000) {
                        volume=marketCapBean.getVolume_cny();
                    } else if (new BigDecimal(marketCapBean.getVolume_cny()).floatValue() < 100000000) {
                        volume=new BigDecimal(marketCapBean.getVolume_cny()).divide(new BigDecimal(10000)).setScale(2, RoundingMode.HALF_UP).toPlainString()+ App.get().getString(R.string.wan);
                    } else {
                        if (App.get().isZh()){
                            volume=new BigDecimal(marketCapBean.getVolume_cny()).divide(new BigDecimal(100000000)).setScale(2, RoundingMode.HALF_UP).toPlainString()+App.get().getString(R.string.yi);
                        }else {
                            volume=new BigDecimal(marketCapBean.getVolume_cny()).divide(new BigDecimal(1000000000)).setScale(2, RoundingMode.HALF_UP).toPlainString()+App.get().getString(R.string.yi);
                        }
                    }
                    liang.setText("¥"+volume);
                }else {
                    price.setText("$"+marketCapBean.getPrice());
                    hight.setText("$"+marketCapBean.getHigh_price());
                    low.setText("$"+marketCapBean.getLow_price());
                    String volume="0.00";
                    if (new BigDecimal(marketCapBean.getVolume()).floatValue() < 10000) {
                        volume=marketCapBean.getVolume();
                    } else if (new BigDecimal(marketCapBean.getVolume()).floatValue() < 100000000) {
                        volume=new BigDecimal(marketCapBean.getVolume()).divide(new BigDecimal(10000)).setScale(2, RoundingMode.HALF_UP).toPlainString()+ App.get().getString(R.string.wan);
                    } else {
                        if (App.get().isZh()){
                            volume=new BigDecimal(marketCapBean.getVolume()).divide(new BigDecimal(100000000)).setScale(2, RoundingMode.HALF_UP).toPlainString()+App.get().getString(R.string.yi);
                        }else {
                            volume=new BigDecimal(marketCapBean.getVolume()).divide(new BigDecimal(1000000000)).setScale(2, RoundingMode.HALF_UP).toPlainString()+App.get().getString(R.string.yi);
                        }
                    }
                    liang.setText("$"+volume);
                }
                if (marketCapBean.getChange().contains("-")){
                    charge.setTextColor(Color.parseColor("#F29A1E"));
                    charge.setText(marketCapBean.getChange());
                }else {
                    charge.setTextColor(Color.parseColor("#4D9478"));
                    charge.setText("+"+marketCapBean.getChange());
                }

                paiming.setText(marketCapBean.getRank()+"");
                if (App.get().getUnit()==1){
                    String volume="0.00";
                    if (new BigDecimal(marketCapBean.getMarket_cny()).floatValue() < 10000) {
                        volume=marketCapBean.getMarket_cny();
                    } else if (new BigDecimal(marketCapBean.getMarket_cny()).floatValue() < 100000000) {
                        volume=new BigDecimal(marketCapBean.getMarket_cny()).divide(new BigDecimal(10000)).setScale(2, RoundingMode.HALF_UP).toPlainString()+ App.get().getString(R.string.wan);
                    } else {
                        if (App.get().isZh()){
                            volume=new BigDecimal(marketCapBean.getMarket_cny()).divide(new BigDecimal(100000000)).setScale(2, RoundingMode.HALF_UP).toPlainString()+App.get().getString(R.string.yi);
                        }else {
                            volume=new BigDecimal(marketCapBean.getMarket_cny()).divide(new BigDecimal(1000000000)).setScale(2, RoundingMode.HALF_UP).toPlainString()+App.get().getString(R.string.yi);
                        }
                    }
                    shizhi.setText("¥"+volume);
                }else {
                    String volume="0.00";
                    if (new BigDecimal(marketCapBean.getMarket()).floatValue() < 10000) {
                        volume=marketCapBean.getMarket();
                    } else if (new BigDecimal(marketCapBean.getMarket()).floatValue() < 100000000) {
                        volume=new BigDecimal(marketCapBean.getMarket()).divide(new BigDecimal(10000)).setScale(2, RoundingMode.HALF_UP).toPlainString()+ App.get().getString(R.string.wan);
                    } else {
                        if (App.get().isZh()){
                            volume=new BigDecimal(marketCapBean.getMarket()).divide(new BigDecimal(100000000)).setScale(2, RoundingMode.HALF_UP).toPlainString()+App.get().getString(R.string.yi);
                        }else{
                            volume=new BigDecimal(marketCapBean.getMarket()).divide(new BigDecimal(1000000000)).setScale(2, RoundingMode.HALF_UP).toPlainString()+App.get().getString(R.string.yi);
                        }
                    }
                    shizhi.setText("$"+volume);
                }
                liutongliang.setText(marketCapBean.getLiquidity()+" "+marketCapBean.getSymbol());
                zongliang.setText(marketCapBean.getCirculation()+" "+marketCapBean.getSymbol());

                //加载交易市场
                getMarket();
            }
        });
    }

    private void getMarket() {
        ZixunApi.getMarkets(this, marketCapBean.getSymbol(), new JsonCallback<LzyResponse<ArrayList<ProjectMarketBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<ProjectMarketBean>>> response) {
                data.clear();
                if (null!=response.body().data){
                    data.addAll(response.body().data);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response<LzyResponse<ArrayList<ProjectMarketBean>>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
            }

        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
