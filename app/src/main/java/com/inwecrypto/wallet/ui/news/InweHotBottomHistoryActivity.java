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
import com.inwecrypto.wallet.bean.ArticleTagsBean;
import com.inwecrypto.wallet.bean.TradingProjectDetaileBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.adapter.CommonPagerAdapter;
import com.inwecrypto.wallet.ui.news.fragment.ProjectBottomHistoryFragment;
import com.inwecrypto.wallet.ui.news.fragment.ProjectHistoryFragment;
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

/**
 * 作者：xiaoji06 on 2018/2/9 16:25
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class InweHotBottomHistoryActivity extends BaseActivity {

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

    private List<String> mDataList = new ArrayList<>();

    private ArrayList<BaseFragment> fragments;
    private ProjectBottomHistoryFragment fragment1;

    private CommonPagerAdapter adapter;
    private TradingProjectDetaileBean project;

    @Override
    protected void getBundleExtras(Bundle extras) {
        project= (TradingProjectDetaileBean) extras.getSerializable("project");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.project_history_activity;
    }

    @Override
    protected void initView() {

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(R.string.lishizixun);
        txtRightTitle.setVisibility(View.GONE);

        magicIndicator.setBackgroundColor(Color.parseColor("#FAFAFA"));

        mDataList.clear();
        mDataList.add(getString(R.string.quanbu));
        mDataList.add(getString(R.string.jiaoyisuogonggao));
        mDataList.add(getString(R.string._24xiaoshizixun));
        mDataList.add(getString(R.string.xinwen));

        ProjectBottomHistoryFragment fragment1 = new ProjectBottomHistoryFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("type",1);
        fragment1.setArguments(bundle1);

        ProjectBottomHistoryFragment fragment2 = new ProjectBottomHistoryFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("type",2);
        fragment2.setArguments(bundle2);

        ProjectBottomHistoryFragment fragment3 = new ProjectBottomHistoryFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putInt("type",3);
        fragment3.setArguments(bundle3);

        ProjectBottomHistoryFragment fragment4 = new ProjectBottomHistoryFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putInt("type",4);
        fragment4.setArguments(bundle4);

        fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);

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
                simplePagerTitleView.setPadding(simplePagerTitleView.getPaddingLeft(),simplePagerTitleView.getPaddingTop(),simplePagerTitleView.getPaddingRight(),UIUtil.dip2px(context, 4));
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

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
