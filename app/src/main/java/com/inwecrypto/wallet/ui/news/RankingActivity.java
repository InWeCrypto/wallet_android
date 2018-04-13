package com.inwecrypto.wallet.ui.news;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.MarketPriceBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.adapter.CommonPagerAdapter;
import com.inwecrypto.wallet.ui.news.fragment.Ranking1Fragment;
import com.inwecrypto.wallet.ui.news.fragment.Ranking2Fragment;
import com.inwecrypto.wallet.ui.news.fragment.Ranking3Fragment;
import com.inwecrypto.wallet.ui.news.fragment.Ranking4Fragment;
import com.inwecrypto.wallet.ui.news.fragment.Ranking5Fragment;
import com.lzy.okgo.model.Response;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/2/9 16:25
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class RankingActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.market)
    TextView market;

    private List<String> mDataList = new ArrayList<>();

    private ArrayList<BaseFragment> fragments;

    private CommonPagerAdapter adapter;

    @Override
    protected void getBundleExtras(Bundle extras) {
    }

    @Override
    protected int setLayoutID() {
        return R.layout.ranking_activity;
    }

    @Override
    protected void initView() {

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(R.string.paihang);
        txtRightTitle.setVisibility(View.GONE);

        mDataList.clear();
        mDataList.add(getString(R.string.shizhizhangfu));
        mDataList.add(getString(R.string.jiaoyisuo));
        mDataList.add(getString(R.string.dapp));
        mDataList.add(getString(R.string.inwexiangmupaihang));
        mDataList.add(getString(R.string.ermenzixun));

        Ranking1Fragment fragment1 = new Ranking1Fragment();
        Ranking2Fragment fragment2 = new Ranking2Fragment();
        Ranking3Fragment fragment3 = new Ranking3Fragment();
        Ranking4Fragment fragment4 = new Ranking4Fragment();
        Ranking5Fragment fragment5 = new Ranking5Fragment();

        fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        fragments.add(fragment5);

        adapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);

        CommonNavigator commonNavigator = new CommonNavigator(mActivity);
        commonNavigator.setScrollPivotX(0.25f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#626262"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#F46A00"));
                simplePagerTitleView.setTextSize(14);
                simplePagerTitleView.setPadding(simplePagerTitleView.getPaddingLeft(), simplePagerTitleView.getPaddingTop(), simplePagerTitleView.getPaddingRight(), UIUtil.dip2px(context, 4));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setYOffset(UIUtil.dip2px(context, 10));
                indicator.setColors(Color.parseColor("#F46A00"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    @Override
    protected void initData() {
        ZixunApi.getMarketPrice(this, new JsonCallback<LzyResponse<MarketPriceBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<MarketPriceBean>> response) {
               market.setText(getString(R.string.shizhi)+":$"+response.body().data.getTotal_market_cap_by_available_supply_usd().replaceAll(".[0-9]+?$", "")
               +"    "+getString(R.string.paihang_jiaoyiliang24)+":$"+response.body().data.getTotal_volume_usd().replaceAll(".[0-9]+?$", ""));
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
