package com.inwecrypto.wallet.ui.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.adapter.CommonPagerAdapter;
import com.inwecrypto.wallet.ui.wallet.fragment.MessageListFragment;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class MessageActivity extends BaseActivity {
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.rb_eth)
    RadioButton rbEth;
    @BindView(R.id.rb_btc)
    RadioButton rbBtc;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.vp_list)
    ViewPager vpList;

    private ArrayList<BaseFragment> fragments;
    private MessageListFragment ethMailListFragment;
    private MessageListFragment btcMailListFragment;
    private CommonPagerAdapter adapter;

    @Override
    protected void getBundleExtras(Bundle extras) {
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_message;
    }

    @Override
    protected void initView() {

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(R.string.xinxi);

        txtRightTitle.setVisibility(View.GONE);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_eth:
                        vpList.setCurrentItem(0);
                        break;
                    case R.id.rb_btc:
                        vpList.setCurrentItem(1);
                        break;
                }
            }
        });

        vpList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        rg.check(R.id.rb_eth);
                        break;
                    case 1:
                        rg.check(R.id.rb_btc);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ethMailListFragment=new MessageListFragment();
        Bundle ethBundle=new Bundle();
        ethBundle.putInt("type",0);
        ethMailListFragment.setArguments(ethBundle);

        btcMailListFragment=new MessageListFragment();
        Bundle btcBundle=new Bundle();
        btcBundle.putInt("type",1);
        btcMailListFragment.setArguments(btcBundle);

        fragments=new ArrayList<>();
        fragments.add(ethMailListFragment);
        fragments.add(btcMailListFragment);

        adapter=new CommonPagerAdapter(getSupportFragmentManager(),fragments);
        vpList.setAdapter(adapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_MESSAGE));
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }


}
