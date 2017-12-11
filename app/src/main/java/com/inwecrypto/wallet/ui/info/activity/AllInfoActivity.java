package com.inwecrypto.wallet.ui.info.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.info.fragment.AllInfoFragment;
import com.inwecrypto.wallet.ui.me.adapter.CommonPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2017/11/13 17:14
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class AllInfoActivity extends BaseActivity {


    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.serch)
    FrameLayout serch;
    @BindView(R.id.menu)
    FrameLayout menu;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.vp_list)
    ViewPager vpList;
    @BindView(R.id.line)
    LinearLayout line;

    private ArrayList<BaseFragment> fragments;
    private AllInfoFragment allFragment;
    private AllInfoFragment tuwenFragment;
    private AllInfoFragment shipinFragment;
    private AllInfoFragment kuaixunFragment;
    private CommonPagerAdapter adapter;

    private int lineWidth;

    @Override
    protected void getBundleExtras(Bundle extras) {
    }

    @Override
    protected int setLayoutID() {
        return R.layout.info_activity_all_info_layout;
    }

    @Override
    protected void initView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.showMainMenu(menu, AllInfoActivity.this, true);
            }
        });

        serch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, InfoWebActivity.class);
                intent.putExtra("type", 1);
                keepTogo(intent);
            }
        });

        title.setText(R.string.suoyouzixun);

        state.setVisibility(View.GONE);

        lineWidth=(ScreenUtils.getScreenWidth(mActivity)- DensityUtil.dip2px(this,160))/4;

        line.setLayoutParams(new FrameLayout.LayoutParams((ScreenUtils.getScreenWidth(mActivity)- DensityUtil.dip2px(this,160))/4, ViewGroup.LayoutParams.WRAP_CONTENT));

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                FrameLayout.LayoutParams params= (FrameLayout.LayoutParams) line.getLayoutParams();
                switch (checkedId) {
                    case R.id.suoyou:
                        vpList.setCurrentItem(0);
                        params.leftMargin=0;
                        break;
                    case R.id.tuwen:
                        vpList.setCurrentItem(1);
                        params.leftMargin=lineWidth;
                        break;
                    case R.id.shipin:
                        vpList.setCurrentItem(2);
                        params.leftMargin=lineWidth*2;
                        break;
                    case R.id.kuaixun:
                        vpList.setCurrentItem(3);
                        params.leftMargin=lineWidth*3;
                        break;
                }
                line.setLayoutParams(params);
            }
        });

        vpList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                FrameLayout.LayoutParams params= (FrameLayout.LayoutParams) line.getLayoutParams();
                switch (position) {
                    case 0:
                        rg.check(R.id.suoyou);
                        params.leftMargin=0;
                        break;
                    case 1:
                        rg.check(R.id.tuwen);
                        params.leftMargin=lineWidth;
                        break;
                    case 2:
                        rg.check(R.id.shipin);
                        params.leftMargin=lineWidth*2;
                        break;
                    case 3:
                        rg.check(R.id.kuaixun);
                        params.leftMargin=lineWidth*3;
                        break;
                }
                line.setLayoutParams(params);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        allFragment = new AllInfoFragment();
        Bundle allBundle = new Bundle();
        allBundle.putString("type", "all");
        allFragment.setArguments(allBundle);

        tuwenFragment = new AllInfoFragment();
        Bundle tuwenBundle = new Bundle();
        tuwenBundle.putString("type", "img-txt");
        tuwenFragment.setArguments(tuwenBundle);

        shipinFragment = new AllInfoFragment();
        Bundle shipinBundle = new Bundle();
        shipinBundle.putString("type", "video");
        shipinFragment.setArguments(shipinBundle);

        kuaixunFragment = new AllInfoFragment();
        Bundle kuaixunBundle = new Bundle();
        kuaixunBundle.putString("type", "txt");
        kuaixunFragment.setArguments(kuaixunBundle);

        fragments = new ArrayList<>();
        fragments.add(allFragment);
        fragments.add(tuwenFragment);
        fragments.add(shipinFragment);
        fragments.add(kuaixunFragment);

        adapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);
        vpList.setAdapter(adapter);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
