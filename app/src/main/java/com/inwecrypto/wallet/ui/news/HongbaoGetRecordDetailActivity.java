package com.inwecrypto.wallet.ui.news;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.DrawRecordBean;
import com.inwecrypto.wallet.bean.HongbaoDetailBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.adapter.GetHongbaoDetailAdapter;
import com.lzy.okgo.model.Response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/4/24 15:42
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class HongbaoGetRecordDetailActivity extends BaseActivity {
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.hit)
    TextView hit;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.get_num)
    TextView getNum;
    @BindView(R.id.totle)
    TextView totle;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.bg)
    ImageView bg;
    @BindView(R.id.hit1)
    TextView hit1;
    @BindView(R.id.scroll)
    NestedScrollView scroll;
    @BindView(R.id.titlefl)
    FrameLayout titlefl;

    private GetHongbaoDetailAdapter adapter;
    private ArrayList<HongbaoDetailBean.DrawsBean> data = new ArrayList<>();
    private int titleHight;
    private int bgHight;

    private DrawRecordBean redbag;

    @Override
    protected void getBundleExtras(Bundle extras) {
        redbag = (DrawRecordBean) extras.getSerializable("redbag");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.hongbao_get_record_detail_activity;
    }

    @Override
    protected void initView() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int hight = (int) (ScreenUtils.getScreenWidth(this) / 2250.0f * 687.0f);
        bgHight=hight;
        bg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, hight));

        titlefl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                titlefl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                titleHight=titlefl.getMeasuredHeight();
            }
        });

        scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("aaaa","scrollY:"+scrollY+" bgHight:"+bgHight+" titleHight:"+titleHight);
                int alpha= (int) ((scrollY)*1.0f/(bgHight-titleHight)*1.0f*255);
                if (alpha>=255){
                    alpha=255;
                }
                if (alpha<0){alpha=0;}
                String al=new BigInteger(alpha+"",10).toString(16);
                String color="#"+(al.length()==1?("0"+al):al)+"B23E2E";
                Log.e("bbb",color);
                // 只是layout背景透明(仿知乎滑动效果)
                titlefl.setBackgroundColor(Color.parseColor(color));
            }
        });

        Glide.with(this).load(redbag.getRedbag().getGnt_category().getIcon()).crossFade().into(icon);
        if (App.get().isZh()){
            hit.setText("你已经成功领取了" + redbag.getRedbag().getShare_user() + "的红包");
            hit1.setText(Html.fromHtml("红包将在<font color='#F9480E'>24H</font>内开奖，请留意相关钱包关注领取情况"));
        }else {
            hit.setText("You have already opened the Packet " + redbag.getRedbag().getShare_user() + "'s Red Packet");
            hit1.setText(Html.fromHtml("The Red Packet will be launched in <font color='#F9480E'>24H</font>, Please keep watching the info of your wallet"));
        }
        address.setText(redbag.getRedbag_addr());

        if (redbag.getRedbag().getDone() == 0 || redbag.getRedbag().getDone() == 1) {//待开奖
            num.setText("***" + redbag.getRedbag().getRedbag_symbol());
        } else {//已开奖
            String price = new BigDecimal(AppUtil.toD(redbag.getValue().replace("0x", "0"))).divide(AppUtil.decimal(redbag.getRedbag().getGnt_category().getDecimals() + ""), 4, RoundingMode.DOWN).toPlainString();
            num.setText(price + redbag.getRedbag().getRedbag_symbol());
            hit1.setVisibility(View.INVISIBLE);
        }

        getNum.setText(getString(R.string.lingqushuliang)+" 0/" + redbag.getRedbag().getRedbag_number());

        adapter = new GetHongbaoDetailAdapter(this, R.layout.get_hongbao_record_detail_adapter, data, redbag.getDraw_addr(), redbag.getRedbag().getRedbag_symbol(), redbag.getRedbag().getDone() == 2 ? true : false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        list.setNestedScrollingEnabled(false);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
    }

    @Override
    protected void initData() {
        ZixunApi.getSendRecord(this, redbag.getRedbag().getId(), new JsonCallback<LzyResponse<HongbaoDetailBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<HongbaoDetailBean>> response) {

                data.clear();
                if (null != response.body().data.getDraws()) {
                    data.addAll(response.body().data.getDraws());
                }

                getNum.setText(getString(R.string.lingqushuliang)+" " + data.size() + "/" + redbag.getRedbag().getRedbag_number());
                if (redbag.getRedbag().getDone() == 0 || redbag.getRedbag().getDone() == 1) {//待开奖
                    totle.setText("***" + redbag.getRedbag().getRedbag_symbol());
                    adapter.setIsOpen(false);
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.setDecimal(response.body().data.getGnt_category().getDecimals() + "");
                    adapter.setIsOpen(true);
                    adapter.notifyDataSetChanged();

                    BigDecimal totlePrice = new BigDecimal(0);
                    for (HongbaoDetailBean.DrawsBean drawsBean : data) {
                        totlePrice = totlePrice.add(new BigDecimal(AppUtil.toD(drawsBean.getValue().replace("0x", "0"))));
                    }
                    totle.setText(totlePrice.divide(AppUtil.decimal(redbag.getRedbag().getGnt_category().getDecimals() + ""), 4, RoundingMode.DOWN).toPlainString() + redbag.getRedbag().getRedbag_symbol());
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
