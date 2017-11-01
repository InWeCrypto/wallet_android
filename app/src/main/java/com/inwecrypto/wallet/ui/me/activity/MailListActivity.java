package com.inwecrypto.wallet.ui.me.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.adapter.CommonPagerAdapter;
import com.inwecrypto.wallet.ui.me.fragment.MailListFragment;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class MailListActivity extends BaseActivity {
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
    private MailListFragment ethMailListFragment;
    private MailListFragment btcMailListFragment;
    private CommonPagerAdapter adapter;
    private boolean address;

    @Override
    protected void getBundleExtras(Bundle extras) {
        address=extras.getBoolean("address",false);
    }

    @Override
    protected int setLayoutID() {
        return R.layout.me_activity_mail_list;
    }

    @Override
    protected void initView() {

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(R.string.mail_title);

        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,MailDetaileActivity.class);
                intent.putExtra("isAdd",true);
                keepTogo(intent);
            }
        });

        Drawable drawableInfo= getResources().getDrawable(R.mipmap.btn_add_friends);
        /// 这一步必须要做,否则不会显示.
        drawableInfo.setBounds(0, 0, drawableInfo.getMinimumWidth(), drawableInfo.getMinimumHeight());
        txtRightTitle.setCompoundDrawables(drawableInfo,null,null,null);

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

        ethMailListFragment=new MailListFragment();
        Bundle ethBundle=new Bundle();
        ethBundle.putBoolean("isEth",true);
        ethBundle.putBoolean("address",address);
        ethMailListFragment.setArguments(ethBundle);

        btcMailListFragment=new MailListFragment();
        Bundle btcBundle=new Bundle();
        btcBundle.putBoolean("isEth",false);
        btcBundle.putBoolean("address",address);
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
    protected void EventBean(BaseEventBusBean event) {

    }


}
